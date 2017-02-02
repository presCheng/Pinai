package com.jl.atys.dsgy;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jl.opengallery.NativeImageLoader;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * User: GunnarXu
 * Desc: 插入图片GridView适配器
 * Date: 2015-01-09
 * Time: 16:01
 * FIXME
 */
public class InsertPicAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public InsertPicAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_pic, parent, false);
        }
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
        String filename = mDatas.get(position);
        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(filename, new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {

            }
        });

        imageView.setImageBitmap(bitmap);
        return convertView;
    }
}
