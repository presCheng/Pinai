package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：Accept
 * 类描述：接受或拒绝简历
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-13
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class RecoveryPassword {

    public RecoveryPassword(String phone, String password, final SuccessCallback successCallback,
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
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail("该用户没有注册");
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
        }, Config.KEY_ACTION, Config.ACTION_RECOVERYPASSWORD, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_PHONE, phone,
                Config.KEY_PHONE_PASSWORD, password);
    }

    public interface SuccessCallback {
        void onSuccess();
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
