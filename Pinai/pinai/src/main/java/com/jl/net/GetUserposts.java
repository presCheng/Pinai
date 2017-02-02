package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.MyPostBean;
import com.jl.domain.PostBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：GetPserportraits
 * 类描述：我的帖子
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-13
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetUserposts {

    public GetUserposts(String id, final SuccessCallback successCallback,
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
                                        JSONObject o = obj.getJSONObject("data");
                                        MyPostBean myPostBean = new MyPostBean();
                                        List<PostBean> data = new ArrayList<PostBean>();
                                        myPostBean.setPortrait(o.getString("portrait"));
                                        myPostBean.setNickname(o.getString("nickname"));
                                        myPostBean.setPostsCount(o.getString("posts_count"));
                                        JSONArray array = o.getJSONArray("posts");
                                        for (int i = 0; i < array.length(); i++) {
                                            PostBean post = new PostBean();
                                            JSONObject p = array.getJSONObject(i);
                                            post.setId(p.getString("id"));
                                            post.setTitle(p.getString("title"));
                                            post.setCreatedAt(p.getString("created_at"));
                                            post.setCommentsCount(p.getString("comments_count"));
                                            data.add(post);
                                        }
                                        myPostBean.setDatas(data);
                                        successCallback.onSuccess(myPostBean);
                                    }
                                    break;

                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("失败");
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (failCallback != null) {
                                failCallback.onFail("解析错误");
                            }
                        }
                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail("未知错误");
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_GET_USERPOSTS, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(MyPostBean myPostBean);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
