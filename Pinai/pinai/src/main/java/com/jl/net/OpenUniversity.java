package com.jl.net;

import com.jl.atys.chat.domain.OpenUnver;
import com.jl.basic.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：获得开房学校
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-9 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class OpenUniversity {
    public OpenUniversity(final SuccessCallback successCallback,
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
                                        List<OpenUnver> datas = new ArrayList<OpenUnver>();
                                        JSONArray data = obj.getJSONArray("data");
                                        for (int i = 0; i < data.length(); i++) {
                                            OpenUnver ou = new OpenUnver();
                                            JSONObject o = data.getJSONObject(i);
                                            ou.setId(o.getString("id"));
                                            ou.setUniversity(o.getString("university"));
                                            ou.setStatus(o.getString("status"));
                                            ou.setOpenAt(o.getString("open_at"));
                                            datas.add(ou);
                                        }
                                        successCallback.onSuccess(datas);
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
        }, Config.KEY_ACTION, Config.ACTION_OPEN_UNIVERSITY, Config.KEY_TOKEN,
                Config.TOKEN);
    }

    public interface SuccessCallback {
        void onSuccess(List<OpenUnver> datas);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
