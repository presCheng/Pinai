package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：ReNew
 * 类描述：签到
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-9
 * 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ReNew {

    public ReNew(String id, String dorenew,
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
                                        successCallback.onSuccess(obj.getString("renewdays"));
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail("你已经签到过了");
                                    }
                                    break;
                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("未知异常");
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
        }, Config.KEY_ACTION, Config.ACTION_RENEW, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id, Config.KEY_DORENEW, dorenew);
    }

    public interface SuccessCallback {
        void onSuccess(String reNewDays);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
