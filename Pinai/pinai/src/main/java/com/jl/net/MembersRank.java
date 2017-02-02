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
public class MembersRank {

    /**
     * @param lastid          最后一个id
     * @param perpage         每页数量
     * @param successCallback 成功回调
     * @param failCallback    失败回调
     */
    public MembersRank(String lastid, String perpage,String userid,
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
                                        data.add(gd);
                                    }
                                    String rank;
                                    try {
                                        rank =obj.getString("rank");
                                    }catch (Exception e){
                                        rank="0";
                                    }

                                    if (successCallback != null) {
                                        successCallback.onSuccess(data,rank);
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
                        }
                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_MEMBERSRANK,
                Config.KEY_TOKEN, Config.TOKEN, Config.KEY_LASTID, lastid,
                Config.KEY_PERPAGE, perpage,  Config.KEY_USERID, userid);
    }

    public interface SuccessCallback {
        void onSuccess(List<GoPinData> data, String rank);
    }

    public interface FailCallback {
        void onFail();
    }
}
