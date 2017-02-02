package com.jl.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.jl.opengallery.NativeImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * 把数据中的图片路径中的图片转换程base64传到服务器
 */
public class XuDecodeBase64 {
    private String pathBase64;

    public String getPathBase64() {
        return pathBase64;
    }

    public XuDecodeBase64 invoke(List<String> pathAll) {
        JSONObject images = new JSONObject();
        try {
            for (int i = 0; i < pathAll.size(); i++) {
                String path = pathAll.get(i);
                Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, null);
                int dot = path.lastIndexOf('.');
                String doc = "";
                if ((dot > -1) && (dot < (path.length() - 1))) {
                    doc = path.substring(dot + 1);
                    Log.e("e", doc);
                }
                JSONArray image = new JSONArray();
                image.put(doc).put(UserTools.imgToBase64(bitmap));
                images.put("img" + (i), image);
            }
        } catch (Exception e) {
            Log.i("e", "图片解析异常");
        }
        pathBase64 = images.toString();
        return this;
    }

    public XuDecodeBase64 invoke(Bitmap bitmap) {
        try {
            pathBase64 = UserTools.imgToBase64(bitmap);
        } catch (Exception e) {
            Log.i("e", "图片解析异常");
        }
        return this;
    }


}