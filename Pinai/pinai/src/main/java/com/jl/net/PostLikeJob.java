package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：PostLikeJob
 * 类描述：发帖
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-13
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class PostLikeJob {

    public PostLikeJob(String userid, String title, String content, String rule_1, String rule_2, String rule_3, String rule_4, String rule_5, final SuccessCallback successCallback,
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
                                        successCallback.onSuccess();
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
        }, Config.KEY_ACTION, Config.ACTION_POST_LIKE_JOB,
                Config.KEY_TOKEN,Config.TOKEN,
                Config.KEY_USERID, userid,
                Config.KEY_TITLE, title,
                Config.KEY_CONTENT, content,
                Config.KEY_RULE1, rule_1,
                Config.KEY_RULE2, rule_2,
                Config.KEY_RULE3, rule_3,
                Config.KEY_RULE4, rule_4,
                Config.KEY_RULE5, rule_5
        );
    }

    public interface SuccessCallback {
        void onSuccess();
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
