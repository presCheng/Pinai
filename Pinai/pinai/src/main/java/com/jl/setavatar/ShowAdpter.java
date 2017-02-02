package com.jl.setavatar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hrb.jl.pinai.R;

public class ShowAdpter extends BaseAdapter {
    protected LayoutInflater mInflater;
    private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
    /**
     * 用来存储图片的选中情况
     */
    private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
    private GridView mGridView;
    private List<String> list;
    private Context context;
    private List<String> pathAll;//图片路径s

    public ShowAdpter(Context context, List<String> list, GridView mGridView) {
        this.list = list;
        this.context = context;
        this.mGridView = mGridView;
        mInflater = LayoutInflater.from(context);
        pathAll = new ArrayList<String>();
    }


    /**
     * 获得所有选中的图片路径
     *
     * @return
     */
    public List<String> getPathAll() {
        return pathAll;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final String path = list.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_show, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ShowImage) convertView
                    .findViewById(R.id.child_image);
            // 用来监听ImageView的宽和高
            viewHolder.mImageView.setOnMeasureListener(new ShowImage.OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView
                    .setImageResource(R.drawable.empty_photo);
        }
        viewHolder.mImageView.setTag(path);
        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,
                mPoint, new NativeImageLoader.NativeImageCallBack() {
                    @Override
                    public void onImageLoader(final Bitmap bitmap,
                                              final String path) {
                        ImageView mImageView = (ImageView) mGridView
                                .findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });
        if (bitmap != null) {
            viewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            viewHolder.mImageView
                    .setImageResource(R.drawable.empty_photo);
        }
//----------------------------------
//        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Tag",v.getTag()+"");
//            }
//        });
        return convertView;
    }

    /**
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
     *
     * @param view
     */
    private void addAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
                1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    /**
     * 获取选中的Item的position
     *
     * @return
     */
    public List<Integer> getSelectItems() {
        List<Integer> list = new ArrayList<Integer>();
        for (Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet()
                .iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Boolean> entry = it.next();
            if (entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    public static class ViewHolder {
        public ShowImage mImageView;
    }


}
