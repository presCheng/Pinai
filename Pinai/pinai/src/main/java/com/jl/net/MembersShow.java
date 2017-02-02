package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.PersonBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：显示用户详细资料
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-9
 * 下午8:01:26
 * 修改备注：
 *
 * @version 1.0.0
 */
public class MembersShow {

    public MembersShow(String phone, String userid,
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

                                    PersonBean personBean = new PersonBean(obj
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
                                    personBean.setLike(obj.getString("like"));
                                    personBean.setUserLikeMe(obj.getString("user_like_me"));
                                    personBean.setAnswer(obj.getString("answer"));
                                    personBean.setUserId(obj.getString("user_id"));
                                    personBean.setPoints(obj.getString("points"));
                                    personBean.setSalary(obj.getString("salary"));
                                    personBean.setProvince(obj.getString("province"));
                                    if (successCallback != null) {
                                        successCallback.onSuccess(personBean);
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
                            if (failCallback != null) {
                                failCallback.onFail();
                            }
                        }
                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_MEMBERSSHOW,
                Config.KEY_TOKEN, Config.TOKEN, Config.KEY_SENDER_ID, phone,
                Config.KEY_USERID, userid);
    }

    public interface SuccessCallback {
        void onSuccess(PersonBean personBean);
    }

    public interface FailCallback {
        void onFail();
    }
}
