package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.UnReadBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：获得未读的论坛消息
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-9
 * 下午8:01:26
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetNotifications {
    public GetNotifications(String id, String originalNumchars, String numchars, final SuccessCallback successCallback,
                            final FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL, HttpMethod.POST,
                new NetConnection.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            System.out.println(result);
                            JSONObject obj = new JSONObject(result);

                            switch (obj.getInt(Config.KEY_STATUS)) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    if (successCallback != null) {
                                        List<UnReadBean> datas = new ArrayList<UnReadBean>();
                                        JSONArray array = obj.getJSONArray("data");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject o = array.getJSONObject(i);
                                            UnReadBean unReadBean = new UnReadBean();
                                            unReadBean.setId(o.getString("id"));
                                            unReadBean.setNickname(o.getString("nickname"));
                                            unReadBean.setCategory(o.getString("category"));
                                            unReadBean.setSenderId(o.getString("sender_id"));
                                            unReadBean.setReceiverId(o.getString("receiver_id"));
                                            unReadBean.setCategoryId(o.getString("category_id"));
                                            unReadBean.setPostId(o.getString("post_id"));
                                            unReadBean.setCommentId(o.getString("comment_id"));
                                            unReadBean.setReplyId(o.getString("reply_id"));
                                            unReadBean.setCreatedAt(o.getString("created_at"));
                                            unReadBean.setPortrait(o.getString("portrait"));
                                            unReadBean.setContent(o.getString("content"));
                                            unReadBean.setOriginalContent(o.getString("original_content"));
                                            datas.add(unReadBean);
                                        }
                                        successCallback.onSuccess(datas);
                                    }
                                    break;
                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("读取失败");
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (failCallback != null) {
                                failCallback.onFail("解析异常");
                            }
                        }

                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail("网络异常");
                }

            }
        }, Config.KEY_ACTION, Config.ACTION_GET_NOTIFICATION, Config.KEY_TOKEN, Config.TOKEN,
                Config.KEY_ID, id, Config.KEY_ORIGINAL_NUMCHARS, originalNumchars, Config.KEY_NUMCHARS, numchars);
    }

    public interface SuccessCallback {
        void onSuccess(List<UnReadBean> datas);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
