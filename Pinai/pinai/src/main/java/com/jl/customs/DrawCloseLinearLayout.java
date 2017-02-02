package com.jl.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hrb.jl.pinai.R;

/**
 * 类名称：DrawCloseLinearLayout
 * 类描述：自定义带关闭标签
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-25 下午1:35:35
 * 修改备注：
 *
 * @version 1.0.0
 */
public class DrawCloseLinearLayout extends LinearLayout {
    private int ThisI;
    private ImageView imageView;
    private TextView textView;
    private DrawCloseLinearLayout dlt = this;
    private boolean isFocused = false;
    private CloseCallback closeCallback;
    public DrawCloseLinearLayout(Context context) {
        super(context);
    }

    public DrawCloseLinearLayout(Context context, String tag, int color, CloseCallback closeCallback) {
        super(context);
        init(context, tag, color, closeCallback);
    }

    public DrawCloseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * init
     *
     * @param context
     * @param tag           标签名
     * @param colornum      第几个颜色
     * @param closeCallback 回调接口
     * @since 1.0.0
     */
    private void init(Context context, String tag, int colornum, final CloseCallback closeCallback) {
        this.closeCallback=closeCallback;
        int[] color = new int[]{R.drawable.bg_border_t1,
                R.drawable.bg_border_t2,
                R.drawable.bg_border_t3,
                R.drawable.bg_border_t4,
                R.drawable.bg_border_t5
        };
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.draw_close_linearlayout, this);
        imageView = (ImageView) findViewById(R.id.draw_close_iv);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeCallback != null) {
                    closeCallback.onCloseCallback(dlt);
                }
            }
        });
        textView = (TextView) findViewById(R.id.draw_close_tv);
        setBackgroundDrawable(getResources().getDrawable(color[colornum % color.length]));
        textView.setText(tag);
    }

    public void getCloseCallBack(){
        closeCallback.onCloseCallback(dlt);
    }
    public int getThisI() {
        return ThisI;
    }

    public void setThisI(int thisI) {
        ThisI = thisI;
    }

    /**
     * 事件反转
     * isMyFocused
     *
     * @return boolean
     * @since 1.0.0
     */
    public boolean isMyFocused() {
        if (isFocused) {
            isFocused = false;
            return true;
        } else {
            isFocused = true;
            return false;
        }
    }

    /**
     * 设置图片
     * setIv
     * void
     *
     * @since 1.0.0
     */
    public void setIv(int drawable) {
        imageView.setBackgroundResource(drawable);
    }

    public interface CloseCallback {
        void onCloseCallback(DrawCloseLinearLayout drawCloseLinearLayout);
    }
}