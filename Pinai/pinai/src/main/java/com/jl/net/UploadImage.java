package com.jl.net;

import android.util.Log;

import com.jl.basic.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：UploadImage
 * 类描述：上传图片
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-8 下午2:43:57
 * 修改备注：
 *
 * @version 1.0.0
 */
public class UploadImage {

    public UploadImage(String base64Image,
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
                                        JSONArray array = obj.getJSONArray("path");
                                        String imgUrl = "";
                                        for (int i = 0; i < array.length(); i++) {
                                            String url = array.getJSONObject(i).getString("img");
                                            imgUrl += "<img src=\"" + url + "\" _src=\"" + url + "\"  ><br/>";
                                        }
                                        Log.e("img", imgUrl);
                                        successCallback.onSuccess(imgUrl);
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail("失败");
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
        }, Config.KEY_ACTION, Config.ACTION_UPLOADIMAGE, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_DATA, base64Image);
    }

    public interface SuccessCallback {
        void onSuccess(String url);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
