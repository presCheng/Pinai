package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：GetPortrait
 * 类描述：获得头像地址
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-13
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetPortrait {

    public GetPortrait(String id, final SuccessCallback successCallback,
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
                                    String portrait = obj.getString("portrait");
                                    String nickname = obj.getString("nickname");
                                    String points = obj.getString("points");
                                    String verify = obj.getString("is_verify");
                                    if (successCallback != null) {
                                        successCallback.onSuccess(portrait, nickname, verify,points);
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
        }, Config.KEY_ACTION, Config.ACTION_GET_PORTRAIT, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(String portrait, String nickname, String verify, String points);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
