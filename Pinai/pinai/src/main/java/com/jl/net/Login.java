package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：登录 创建人：徐志国 修改人：徐志国 修改时间：2014-11-9 下午8:01:26 修改备注：
 *
 * @version 1.0.0
 */
public class Login {

    public Login(String phone, String password,
                 final SuccessCallback successCallback,
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
                                        String id = obj.getString("id");
                                        String password = obj.getString("password");
                                        String portrait = obj.getString("portrait");
                                        String sex = obj.getString("sex");
                                        successCallback.onSuccess(id, password, portrait, sex);
                                        // =================登录成功==============这里处理
                                    }
                                    break;

                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("密码错误");
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
        }, Config.KEY_ACTION, Config.ACTION_LOGIN, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_PHONE, phone,
                Config.KEY_PHONE_PASSWORD, password);
    }

    public interface SuccessCallback {
        void onSuccess(String id, String password, String portrait, String sex);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
