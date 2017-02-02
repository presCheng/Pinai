package com.jl.opengallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ShowImage extends ImageView {
    private OnMeasureListener onMeasureListener;

    public ShowImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //将图片测量的大小回调到onMeasureSize()方法中
        if (onMeasureListener != null) {
            onMeasureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public interface OnMeasureListener {
        void onMeasureSize(int width, int height);
    }

}
