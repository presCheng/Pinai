package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：获得论坛未读消息数
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-9
 * 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetForumUnread {

    public GetForumUnread(String id, final SuccessCallback successCallback,
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
                                        String num = obj.getString("num");
                                        successCallback.onSuccess(num);
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail();
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
        }, Config.KEY_ACTION, Config.ACTION_GET_FORUM_UNREAD, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(String num);
    }

    public interface FailCallback {
        void onFail();
    }
}
