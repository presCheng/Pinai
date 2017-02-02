package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.GetCatBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：ForumGetCat
 * 类描述：论坛分类
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-7
 * 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ForumGetCat {

    public ForumGetCat(String catid, String lastid, String perpage, String numchars, String userId,
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
                                        List<Object> getCats = new ArrayList<Object>();
                                        JSONObject data = obj.getJSONObject("data");
                                        JSONArray topArray = data.getJSONArray("top");
                                        List<GetCatBean> tops = new ArrayList<GetCatBean>();
                                        for (int i = 0; i < topArray.length(); i++) {
                                            GetCatBean top = new GetCatBean();
                                            JSONObject o = topArray.getJSONObject(i);
                                            top.setPostId(o.getString("id"));
                                            top.setUserId(o.getString("user_id"));
                                            top.setTitle(o.getString("title"));
                                            top.setContent(o.getString("content"));
                                            top.setCreated_at(o.getString("created_at"));
                                            top.setPortrait(o.getString("portrait"));
                                            top.setSex(o.getString("sex"));
                                            top.setComments_count(o.getString("comments_count"));
                                            top.setNickname(o.getString("nickname"));
                                            top.setThumbnails(o.getString("thumbnails"));
                                            tops.add(top);
                                        }
                                        //如果置顶有内容，就添加到数组中
                                        if (tops.size() > 0) {
                                            getCats.add(tops);
                                        }
                                        JSONArray array = data.getJSONArray("items");
                                        for (int i = 0; i < array.length(); i++) {
                                            GetCatBean bbs = new GetCatBean();
                                            JSONObject o = array.getJSONObject(i);
                                            bbs.setPostId(o.getString("id"));
                                            bbs.setUserId(o.getString("user_id"));
                                            bbs.setTitle(o.getString("title"));
                                            bbs.setContent(o.getString("content"));
                                            bbs.setCreated_at(o.getString("created_at"));
                                            bbs.setPortrait(o.getString("portrait"));
                                            bbs.setSex(o.getString("sex"));
                                            bbs.setComments_count(o.getString("comments_count"));
                                            bbs.setNickname(o.getString("nickname"));
                                            bbs.setThumbnails(o.getString("thumbnails"));
                                            getCats.add(bbs);
                                        }
                                        successCallback.onSuccess(getCats);
                                    }

                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        //论坛未开放
                                        failCallback.onFail("2");
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
        }, Config.KEY_ACTION, Config.ACTION_FORUM_GETCAT, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_CATID, catid,
                Config.KEY_LASTID, lastid, Config.KEY_PERPAGE, perpage, Config.KEY_NUMCHARS, numchars, Config.KEY_USERID, userId);
    }

    public interface SuccessCallback {
        void onSuccess(List<Object> data);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
