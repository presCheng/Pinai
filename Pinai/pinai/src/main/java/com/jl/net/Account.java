package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.PersonBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：Account
 * 类描述：获取个人资料
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-4
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class Account {

    public Account(String id, final SuccessCallback successCallback,
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

                                    PersonBean ip = new PersonBean(obj
                                            .getString("sex"), obj
                                            .getString("bio"), obj
                                            .getString("nickname"), obj
                                            .getString("portrait"), obj
                                            .getString("constellation"), obj
                                            .getString("tag_str"), obj
                                            .getString("hobbies"), obj
                                            .getString("grade"), obj
                                            .getString("question"), obj
                                            .getString("self_intro"), obj
                                            .getString("born_year"), obj
                                            .getString("school"), obj
                                            .getString("is_verify"));
                                    ip.setSalary(obj
                                            .getString("salary"));
                                    ip.setProvince(obj
                                            .getString("province"));
                                    ip.setProvinceId(obj
                                            .getString("province_id"));
                                    ip.setSalaryId(obj
                                            .getString("salary_code"));
                                    if (successCallback != null) {
                                        successCallback.onSuccess(ip);
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
        }, Config.KEY_ACTION, Config.ACTION_ACCOUNT,
                Config.KEY_TOKEN, Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(PersonBean ip);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
