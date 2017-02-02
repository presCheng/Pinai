package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.GetPostChildBean;
import com.jl.domain.GetPostReplyBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：ForumGetReply
 * 类描述：帖子详细
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-7
 * 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ForumGetReply {

    public ForumGetReply(String postid, String commentid, final SuccessCallback successCallback,
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
                                        JSONObject data = obj.getJSONObject("data");
                                        GetPostChildBean gc = new GetPostChildBean();
                                        gc.setId(data.getString("id"));
                                        gc.setUserId(data.getString("user_id"));
                                        gc.setNickname(data.getString("user_nickname"));
                                        gc.setCreatedAt(data.getString("created_at"));
                                        gc.setUserPortrait(data.getString("user_portrait"));
                                        gc.setUserSex(data.getString("user_sex"));
                                        gc.setContent(data.getString("content"));
                                        //gc.setCount(data.getString("reply_count"));
                                        JSONArray arr = data.getJSONArray("comment_reply");
                                        List<GetPostReplyBean> listr = new ArrayList<GetPostReplyBean>();
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject or = arr.getJSONObject(i);
                                            GetPostReplyBean reply = new GetPostReplyBean();
                                            reply.setId(or.getString("id"));
                                            reply.setUserid(or.getString("user_id"));
                                            reply.setCreatAt(or.getString("created_at"));
                                            reply.setContent(or.getString("content"));
                                            //reply.setUserportrait(or.getString("portrait"));
                                            //reply.setSex(or.getString("sex"));
                                            listr.add(reply);
                                        }
                                        gc.setReplyList(listr);
                                        successCallback.onSuccess(gc);
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail("错误");
                                    }
                                    break;
                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("错误");
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
        }, Config.KEY_ACTION, Config.ACTION_FORUM_GETREPLY, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_POSTID, postid,
                Config.KEY_COMMENTID, commentid);
    }

    public interface SuccessCallback {
        void onSuccess(GetPostChildBean gc);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
