package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.SentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：InBox
 * 类描述：追我的人
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-10 下午2:43:57
 * 修改备注：
 *
 * @version 1.0.0
 */
public class InBox {

    public InBox(String id, String lastId, String perpage,
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
                                        List<SentBean> list = new ArrayList<SentBean>();
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject o = arr.getJSONObject(i);
                                            SentBean cm = new SentBean();
                                            cm.setId(o.getString("id"));
                                            cm.setSchool(o.getString("school"));
                                            cm.setName(o.getString("name"));
                                            cm.setCount(o.getString("count"));
                                            cm.setCreated_at(o.getString("created_at"));
                                            cm.setPortrait(o.getString("portrait"));
                                            cm.setStatus(o.getString("status"));
                                            list.add(cm);
                                        }
                                        successCallback.onSuccess(list);
                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (failCallback != null) {
                                        failCallback.onFail("积分不足");
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
        }, Config.KEY_ACTION, Config.ACTION_INBOX, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id, Config.KEY_LASTID, lastId,
                Config.KEY_PERPAGE, perpage);
    }

    public interface SuccessCallback {
        void onSuccess(List<SentBean> cm);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
