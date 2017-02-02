package com.jl.net;

import com.jl.atys.dsgy.zph.ZphData;
import com.jl.basic.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：LikeJobs
 * 类描述：查看帖子
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2015-6-12
 * 下午22:30:50
 * 修改备注：
 *
 * @version 1.0.0
 */
public class LikeJobs {

    public LikeJobs(String userid,String lastid, String perpage, String province,final SuccessCallback successCallback,
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
                                        List<ZphData> datas =new ArrayList<>();
                                       JSONArray array= obj.getJSONArray("data");

                                        for (int i=0;i<array.length();i++){
                                            ZphData data =new ZphData();
                                           JSONObject o= array.getJSONObject(i);
                                            data.setPostId(o.getString("id"));
                                            data.setTitle(o.getString("title"));
                                            datas.add(data);
                                        }
                                        successCallback.onSuccess("1",datas);

                                    }
                                    break;
                                case Config.RESULT_STATUS_INVALID:
                                    if (successCallback != null) {
                                        List<ZphData> datas =new ArrayList<>();
                                        JSONArray array= obj.getJSONArray("data");

                                        for (int i=0;i<array.length();i++){
                                            ZphData data =new ZphData();
                                            JSONObject o= array.getJSONObject(i);
                                            data.setPostId(o.getString("id"));
                                            data.setTitle(o.getString("title"));
                                            datas.add(data);
                                        }
                                        successCallback.onSuccess("2",datas);

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
        }, Config.KEY_ACTION, Config.ACTION_LIKE_JOB,
                Config.KEY_USERID,userid,
                Config.KEY_TOKEN, Config.TOKEN,
                Config.KEY_LASTID, lastid,
                Config.KEY_PERPAGE, perpage,Config.KEY_PROVINCE,province
        );
    }
    public interface SuccessCallback {
        void onSuccess(String status, List<ZphData> datas);
    }
    public interface FailCallback {
        void onFail(String error);
    }
}
