package com.jl.net;

import com.jl.basic.Config;
import com.jl.domain.GetLikeJobBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名称：GetLikeJob
 * 类描述：获得帖子
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-9 下午4:16:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetLikeJob {

    public GetLikeJob(String id,final SuccessCallback successCallback,
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
                                        GetLikeJobBean data =new GetLikeJobBean();
                                        data.setTitle(obj.getString("title"));
                                        data.setContent(obj.getString("content"));
                                        data.setPortrait(obj.getString("portrait"));
                                        data.setSex(obj.getString("sex"));
                                        data.setUserId(obj.getString("user_id"));
                                        successCallback.onSuccess(data);
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
        }, Config.KEY_ACTION, Config.ACTION_GET_LIKE_JOB, Config.KEY_TOKEN,
                Config.TOKEN, Config.KEY_ID, id);
    }

    public interface SuccessCallback {
        void onSuccess(GetLikeJobBean data);
    }

    public interface FailCallback {
        void onFail(String error);
    }
}
