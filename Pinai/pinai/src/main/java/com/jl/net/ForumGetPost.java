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
 * 类名称：ForumGetPost
 * 类描述：帖子详细
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-7
 * 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ForumGetPost {

    public ForumGetPost(String postid, String lastid, String perpage, final SuccessCallback successCallback,
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
                                        List<GetPostChildBean> listc = new ArrayList<GetPostChildBean>();
                                        GetPostChildBean first = new GetPostChildBean();
                                        try {
                                            first.setUserPortrait(data.getString("portrait"));
                                            first.setNickname(data.getString("nickname"));
                                            first.setCreatedAt(data.getString("created_at"));
                                            first.setTitle(data.getString("title"));
                                            first.setUserId(data.getString("user_id"));
                                            first.setUserSex(data.getString("sex"));
                                            first.setContent(data.getString("content"));
                                            first.setCount(data.getString("comment_count"));
                                            listc.add(first);
                                        } catch (Exception ignored) {

                                        }

                                        JSONArray carray = data.getJSONArray("comments");
                                        for (int j = 0; j < carray.length(); j++) {
                                            JSONObject oc = carray.getJSONObject(j);
                                            GetPostChildBean child = new GetPostChildBean();
                                            child.setId(oc.getString("id"));
                                            child.setUserId(oc.getString("user_id"));
                                            child.setNickname(oc.getString("user_nickname"));
                                            child.setCreatedAt(oc.getString("created_at"));
                                            child.setUserPortrait(oc.getString("user_portrait"));
                                            child.setUserSex(oc.getString("user_sex"));
                                            child.setContent(oc.getString("content"));
                                            child.setCount(oc.getString("reply_count"));
                                            JSONArray crarray = oc.getJSONArray("comment_reply");
                                            List<GetPostReplyBean> listr = new ArrayList<GetPostReplyBean>();
                                            for (int k = 0; k < crarray.length(); k++) {
                                                JSONObject or = crarray.getJSONObject(k);
                                                GetPostReplyBean reply = new GetPostReplyBean();
                                                reply.setId(or.getString("id"));
                                                reply.setUserid(or.getString("user_id"));
                                                reply.setCreatAt(or.getString("created_at"));
                                                reply.setContent(or.getString("content"));
                                                //reply.setUserportrait(or.getString("portrait"));
                                                //reply.setSex(or.getString("sex"));
                                                listr.add(reply);
                                            }
                                            child.setReplyList(listr);
                                            listc.add(child);
                                        }
                                        successCallback.onSuccess(listc);
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    //帖子被删除返回的是2
                                    if (failCallback != null) {
                                        failCallback.onFail("2");
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
        }, Config.KEY_ACTION, Config.ACTION_FORUM_GETPOST, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_POSTID, postid,
                Config.KEY_LASTID, lastid, Config.KEY_PERPAGE, perpage);
    }

    public interface SuccessCallback {
        void onSuccess(List<GetPostChildBean> listc);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
