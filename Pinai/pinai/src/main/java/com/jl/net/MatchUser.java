package com.jl.net;

import com.jl.atys.dsgy.jly.JlyBean;
import com.jl.basic.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：MatchUser
 * 类描述：简历缘
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-4
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class MatchUser {
    public MatchUser(String id, final SuccessCallback successCallback,
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
                                        List<JlyBean> jlyBeans =new ArrayList<>();
                                        JSONArray arr =obj.getJSONArray("users");
                                        for (int i = 0; i <arr.length() ; i++) {
                                            JlyBean bean =new JlyBean();
                                            JSONObject o = arr.getJSONObject(i);
                                            bean.setId(o.getString("id"));
                                            bean.setBorn_year(o.getString("born_year"));
                                            bean.setNickName(o.getString("nickname"));
                                            bean.setPortrait(o.getString("portrait"));
                                            bean.setSchool(o.getString("school"));
                                            bean.setSex(o.getString("sex"));
                                            jlyBeans.add(bean);
                                        }
                                        String match = obj.getString("match");
                                        successCallback.onSuccess(jlyBeans,match);
                                    }
                                    break;
                                case Config.RESULT_STATUS_ERROR:
                                    if (failCallback != null) {
                                        failCallback.onFail("3");
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (successCallback != null) {
                                        List<JlyBean> jlyBeans =new ArrayList<>();
                                        JSONArray arr =obj.getJSONArray("users");
                                        for (int i = 0; i <arr.length() ; i++) {
                                            JlyBean bean =new JlyBean();
                                            JSONObject o = arr.getJSONObject(i);
                                            bean.setId(o.getString("id"));
                                            bean.setBorn_year(o.getString("born_year"));
                                            bean.setNickName(o.getString("nickname"));
                                            bean.setPortrait(o.getString("portrait"));
                                            bean.setSchool(o.getString("school"));
                                            bean.setSex(o.getString("sex"));
                                            jlyBeans.add(bean);
                                        }
                                        String match = obj.getString("match");
                                        successCallback.onSuccess(jlyBeans,match);
                                    }
                                    break;
                                default:
                                    if (failCallback != null) {
                                        failCallback.onFail("-1");
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
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
        }, Config.KEY_ACTION, Config.ACTION_MATCH_USER,
                Config.KEY_TOKEN, Config.TOKEN, Config.KEY_ID, id);
    }

    public  interface SuccessCallback {
        void onSuccess(List<JlyBean> jlyBeans, String match);
    }

    public  interface FailCallback {
        void onFail(String error);
    }
}
