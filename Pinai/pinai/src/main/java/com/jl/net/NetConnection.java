package com.jl.net;

import android.os.AsyncTask;

import com.jl.basic.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 类描述：与服务器交互主类 创建人：徐志国 修改人：徐志国 修改时间：2014-11-9 下午7:58:26 修改备注：
 *
 * @version 1.0.0
 */
public class NetConnection {
    /**
     * @param url             服务器地址
     * @param method          传输方法
     * @param successCallback 成功回调函数
     * @param failCallback    失败回调函数
     * @param kvs             传入可变参数
     */
    public NetConnection(final String url, final HttpMethod method,
                         final SuccessCallback successCallback,
                         final FailCallback failCallback, final String... kvs) {
        /**
         * params 初始化传入参数 progress 进程进度 result 返回结果，onPostExecute
         */
        new AsyncTask<Void, Void, String>() {
            /**
             * 在后台执行耗时的操作，不能执行ui操作
             */
            @Override
            protected String doInBackground(Void... params) {
                StringBuffer paramsStr = new StringBuffer();
                for (int i = 0; i < kvs.length; i += 2) {
                    paramsStr.append(kvs[i]).append("=").append(kvs[i + 1])
                            .append("&");
                }
                paramsStr.deleteCharAt(paramsStr.length() - 1);
                try {
                    URLConnection uc;
                    switch (method) {
                        case POST:
                            uc = new URL(url).openConnection();
                            // post方法
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(
                                    new OutputStreamWriter(uc.getOutputStream(),
                                            Config.CHARSET));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            break;
                        default:
                            // get方法
                            uc = new URL(url + "?" + paramsStr.toString())
                                    .openConnection();
                            break;
                    }
                    System.out.println("request url:" + uc.getURL());
                    System.out.println("requset data:" + paramsStr);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(uc.getInputStream(),
                                    Config.CHARSET));
                    String line = null;
                    StringBuffer result = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println("result :" + result);
                    return result.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            /**
             * 执行完之后的ui操作
             */
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (successCallback != null) {
                        successCallback.onSuccess(result);
                    }
                } else {
                    if (failCallback != null) {
                        System.out.println("onPostExecute");
                        failCallback.onFail();
                    }
                }

            }
        }.execute();
    }

    public interface SuccessCallback {
        void onSuccess(String result);
    }

    public interface FailCallback {
        void onFail();
    }
}
