package com.jl.net;

import com.jl.basic.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：个人中心信息提交 创建人：徐志国 修改人：徐志国 修改时间：2014-11-23 上午9:43:18 修改备注：
 *
 * @version 1.0.0
 */
public class Complete {
    /**
     * @param nickname      用户昵称（1到30个字符之间）
     * @param constellation 星座
     * @param portrait      头像地址（eg. xxx.jpg）
     * @param tag_str       个人标签 ”,2,7,7”
     * @param sex           性别(man:M woman:F)
     * @param born_year     出生年份 (1993)
     * @param grade         入学年份（2012）
     * @param hobbies       爱好 字符串
     * @param self_intro    自我简介 字符串
     * @param bio           恋爱宣言
     * @param question      爱情考验
     * @param school        学校 数字
     */
    public Complete(String id, String nickname, String constellation,
                    String portrait, String tag_str, String sex, String born_year,
                    String grade, String hobbies, String self_intro, String bio,
                    String question, String school,String salary,String province,
                    final SuccessCallback successCallback,
                    final FailCallback failCallback) {
        new NetConnection(
                Config.SERVER_URL,
                HttpMethod.POST,
                new NetConnection.SuccessCallback() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            switch (obj.getInt(Config.KEY_STATUS)) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    if (successCallback != null) {
                                        successCallback.onSuccess();
                                    }
                                    break;
                                case Config.RESULT_STATUS_FAIL:
                                    if (failCallback != null) {
                                        failCallback
                                                .onFail(Config.RESULT_STATUS_FAIL);
                                    }
                                    break;
                                default:
                                    if (failCallback != null) {
                                        failCallback
                                                .onFail(Config.RESULT_STATUS_INVALID);
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (failCallback != null) {
                                failCallback
                                        .onFail(Config.RESULT_STATUS_INVALID);
                            }
                        }
                    }
                }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail(-1);
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_COMPLETE, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id, Config.KEY_NICKNAME,
                nickname, Config.KEY_CONSTELLATION, constellation,
                Config.KEY_PORTRAIT, "", Config.KEY_TAG_STR, tag_str,
                Config.KEY_SEX, sex, Config.KEY_BORN_YEAR, born_year,
                Config.KEY_GRADE, grade, Config.KEY_HOBBIES, hobbies,
                Config.KEY_SELF_INTRO, self_intro, Config.KEY_BIO, bio,
                Config.KEY_QUESTION, question, Config.KEY_SCHOOL, school,Config.KEY_SALARY,salary,Config.KEY_PROVINCE,province);

    }


    public interface SuccessCallback {
        void onSuccess();
    }

    public interface FailCallback {
        void onFail(int code);
    }
}
