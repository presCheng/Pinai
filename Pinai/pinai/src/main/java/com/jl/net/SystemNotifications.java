package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.AcceptNotificationBean;
import com.jl.domain.FriendNotificationBean;
import com.jl.domain.SystemNotificationBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：获取系统通讯
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-1-4
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SystemNotifications {

    public SystemNotifications(String id, final SuccessCallback successCallback,
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
                                        List<SystemNotificationBean> systemNotificationBeans = new ArrayList<SystemNotificationBean>();
                                        List<FriendNotificationBean> friendNotificationBeans = new ArrayList<FriendNotificationBean>();
                                        List<AcceptNotificationBean> acceptNotificationBeans = new ArrayList<AcceptNotificationBean>();
                                        JSONObject data = obj.getJSONObject("data");
                                        JSONArray systemArray = data.getJSONArray("system_notifications");
                                        JSONArray friendArray = data.getJSONArray("friend_notifications");
                                        JSONArray acceptArray = data.getJSONArray("accept_notifications");
                                        for (int i = 0; i < systemArray.length(); i++) {
                                            JSONObject o = systemArray.getJSONObject(i);
                                            SystemNotificationBean notification = new SystemNotificationBean();
                                            notification.setId(o.getString("id"));
                                            notification.setContent(o.getString("content"));
                                            notification.setCreatedAt(o.getString("created_at"));
                                            systemNotificationBeans.add(notification);
                                        }
                                        for (int i = 0; i < friendArray.length(); i++) {
                                            JSONObject o = friendArray.getJSONObject(i);
                                            FriendNotificationBean notification = new FriendNotificationBean();
                                            notification.setId(o.getString("id"));
                                            notification.setFrom(o.getString("from"));
                                            notification.setContent(o.getString("content"));
                                            notification.setNickname(o.getString("nickname"));
                                            notification.setPortrait(o.getString("portrait"));
                                            notification.setAnswer(o.getString("answer"));
                                            friendNotificationBeans.add(notification);
                                        }
                                        for (int i = 0; i < acceptArray.length(); i++) {
                                            JSONObject o = acceptArray.getJSONObject(i);
                                            AcceptNotificationBean notification = new AcceptNotificationBean();
                                            notification.setId(o.getString("id"));
                                            notification.setFrom(o.getString("from"));
                                            notification.setNickname(o.getString("nickname"));
                                            notification.setPortrait(o.getString("portrait"));
                                            acceptNotificationBeans.add(notification);
                                        }
                                        successCallback.onSuccess(systemNotificationBeans, friendNotificationBeans, acceptNotificationBeans);
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
        }, Config.KEY_ACTION, Config.ACTION_SYSTEM_NOTIFICATIONS,
                Config.KEY_TOKEN, Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(List<SystemNotificationBean> systemNotificationBeans, List<FriendNotificationBean> friendNotificationBeans, List<AcceptNotificationBean> acceptNotificationBeans);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
