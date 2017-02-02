package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.OpenArticleBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：轮播导航图
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-9 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetOpenArticles {

    public GetOpenArticles(
            final SuccessCallback successCallback,
            final FailCallback failCallback) {
        new NetConnection(
                Config.SERVER_URL,
                HttpMethod.POST,
                new NetConnection.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            System.out.println(result);
                            JSONObject obj = new JSONObject(result);
                            switch (obj.getInt(Config.KEY_STATUS)) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    if (successCallback != null) {
                                        List<OpenArticleBean> articles = new ArrayList<OpenArticleBean>();
                                        JSONArray array = obj.getJSONArray("data");
                                        for (int i = 0; i < array.length(); i++) {
                                            OpenArticleBean article = new OpenArticleBean();
                                            JSONObject o = array.getJSONObject(i);
                                            article.setTitle(o.getString("title"));
                                            article.setThumbnails(o.getString("thumbnails"));
                                            article.setUrl(o.getString("url"));
                                            articles.add(article);
                                        }


                                        successCallback.onSuccess(articles);
                                    }
                                    break;
                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail();
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (failCallback != null) {
                                failCallback.onFail();
                            }
                        }
                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_GET_OPEN_ARTICLES, Config.KEY_TOKEN,
                Config.TOKEN);
    }

    public interface SuccessCallback {
        void onSuccess(List<OpenArticleBean> articles);
    }

    public interface FailCallback {
        void onFail();
    }
}
