package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：ForumPostcomment
 * 类描述：追我的人
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-10 下午2:43:57
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ForumPostcomment {
    /**
     * 评论
     */
    public static final String TYPE_COMMENTS = "comments";
    /**
     * 回复楼层
     */
    public static final String TYPE_REPLY = "reply";

    /**
     * @param type            类型 comments reply
     * @param content         内容(都有)
     * @param userid          用户id(都有)
     * @param postid          帖子id(都有)
     * @param replyid         回复id(reply)
     * @param commentid       帖子楼层id(reply)
     * @param successCallback
     * @param failCallback
     */
    public ForumPostcomment(String type, String content, String userid, String postid, String replyid, String commentid,
                            final SuccessCallback successCallback,
                            final FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL, HttpMethod.POST,
                new NetConnection.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        try {
                            System.out.println(result);
                            JSONObject obj = new JSONObject(result);
                            switch (obj.getInt(Config.KEY_STATUS)) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    if (successCallback != null) {
                                        successCallback.onSuccess();
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail("积分不足");
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
                                failCallback.onFail("异常错误");
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
        }, Config.KEY_ACTION, Config.ACTION_FORUM_POSTCOMMENT, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_TYPE, type, Config.KEY_CONTENT, content,
                Config.KEY_USERID, userid, Config.KEY_POSTID, postid,
                Config.KEY_REPLAYID, replyid, Config.KEY_COMMENTID, commentid);
    }

    public interface SuccessCallback {
        void onSuccess();
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
