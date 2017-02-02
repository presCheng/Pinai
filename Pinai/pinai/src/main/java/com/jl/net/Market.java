package com.jl.net;

import com.jl.atys.gopin.GoPinData;
import com.jl.basic.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：缘来在这
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-23
 * 下午12:15:01
 * 修改备注：
 *
 * @version 1.0.0
 */
public class Market {

    /**
     * @param lastid          最后一个id
     * @param perpage         每页数量
     * @param university      学校
     * @param successCallback 成功回调
     * @param failCallback    失败回调
     */
    public Market(String lastid, String perpage, String university,String userId,String born_year,String province,
                  final SuccessCallback successCallback,
                  final FailCallback failCallback) {

        new NetConnection(Config.SERVER_URL, HttpMethod.POST,
                new NetConnection.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            switch (obj.getInt(Config.KEY_STATUS)) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    List<GoPinData> data = new ArrayList<GoPinData>();
                                    JSONArray arr = obj.getJSONArray("data");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject o = (JSONObject) arr.get(i);
                                        GoPinData gd = new GoPinData();
                                        gd.setPortrait(o.getString("portrait"));
                                        gd.setId(o.getString("id"));
                                        gd.setCrenew(o.getString("crenew"));
                                        gd.setIsVerify(o.getString("is_verify"));
                                        gd.setNickname(o.getString("nickname"));
                                        gd.setPoints(o.getString("points"));
                                        gd.setSchool(o.getString("school"));
                                        gd.setSex(o.getString("sex"));
                                        gd.setTag_str(o.getString("tag_str"));
                                        data.add(gd);
                                    }
                                    if (successCallback != null) {
                                        successCallback.onSuccess(data);
                                    }
                                    break;
                                case Config.RESULT_STATUS_FAIL:
                                    if (failCallback != null) {
                                        failCallback.onFail("0");
                                    }
                                    break;

                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("-1");
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail("-1");
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_MARKET,
                Config.KEY_TOKEN, Config.TOKEN, Config.KEY_LASTID, lastid,
                Config.KEY_PERPAGE, perpage, Config.KEY_UNIVERSITY, university,  Config.KEY_USERID, userId,Config.KEY_PROVINCE,province,Config.KEY_BORN_YEAR,born_year);
    }
    public interface SuccessCallback {
        void onSuccess(List<GoPinData> data);
    }
    public interface FailCallback {
        void onFail(String code);
    }
}
