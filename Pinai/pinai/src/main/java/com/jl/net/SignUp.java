package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：注册 创建人：徐志国 修改人：徐志国 修改时间：2014-11-9 下午8:05:47 修改备注：
 *
 * @version 1.0.0
 */
public class SignUp {

    protected static final String FailCallback = null;

    public SignUp(String userName, String password, String sex, String from,
                  final SuccessCallback successCallback,
                  final FailCallback failCallback) {
        // 与服务器通信
        new NetConnection(Config.SERVER_URL, HttpMethod.POST,
                new NetConnection.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            // 根据返回status来处理
                            switch (jsonObject.getInt(Config.KEY_STATUS)) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    if (successCallback != null) {
                                        String id = jsonObject.getString("id");
                                        String password = jsonObject.getString("password");
                                        successCallback.onSuccess(id, password);
                                        // ================成功处理
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
        }, Config.KEY_ACTION, Config.ACTION_SIGN_UP, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_PHONE, userName, Config.KEY_SEX, sex, Config.KEY_FROM, from,
                Config.KEY_PHONE_PASSWORD, password);
    }

    public interface SuccessCallback {
        void onSuccess(String id, String password);
    }

    public interface FailCallback {
        void onFail();
    }
}
