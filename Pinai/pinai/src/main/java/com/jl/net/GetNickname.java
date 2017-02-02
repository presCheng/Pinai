package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.GetNicknameBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 类名称：GetNickname
 * 类描述：
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-10 下午2:43:57
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetNickname {

    public GetNickname(final String id,
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
                                        JSONArray arr = obj.getJSONArray("data");
                                        HashMap<String, GetNicknameBean> map = new HashMap<String, GetNicknameBean>();
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject o = arr.getJSONObject(i);
                                            String id = o.getString("friend_id");
                                            GetNicknameBean idTon = new GetNicknameBean(id, o.getString("nickname"), o.getString("portrait"));
                                            map.put(id, idTon);
                                        }
                                        successCallback.onSuccess(map);
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
        }, Config.KEY_ACTION, Config.ACTION_GET_NICKNAME, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(HashMap<String, GetNicknameBean> idTon);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
