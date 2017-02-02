package com.jl.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hrb.jl.pinai.R;

/**
 * 类名称：DrawLinearLayout
 * 类描述：自定义标签
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-25 下午1:32:05
 * 修改备注：
 *
 * @version 1.0.0
 */
public class DrawLinearLayout extends LinearLayout {

    private TextView textView;

    public DrawLinearLayout(Context context) {
        super(context);
    }

    public DrawLinearLayout(Context context, String tag, int color, CloseCallback closeCallback) {
        super(context);
        init(context, tag, color, closeCallback);
    }

    public DrawLinearLayout(Context context, String tag, CloseCallback closeCallback) {
        super(context);
        init(context, tag, -1, closeCallback);
    }

    public DrawLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * init
     *
     * @param context
     * @param tag           标签名
     * @param colornum      第几个颜色 最后一个颜色是8（这个颜色是灰色）
     * @param closeCallback 回调接口
     * @since 1.0.0
     */
    private void init(Context context, String tag, int colornum, final CloseCallback closeCallback) {
        int[] color = new int[]{R.drawable.bg_border_t1,
                R.drawable.bg_border_t2,
                R.drawable.bg_border_t3,
                R.drawable.bg_border_t4,
                R.drawable.bg_border_t5,
        };
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.draw_linearlayout, this);
        textView = (TextView) findViewById(R.id.draw_tv);

        try {
            //setBackgroundColor(getResources().getColor(color[colornum % color.length]));
            setBackgroundDrawable(getResources().getDrawable(color[colornum % color.length]));
        } catch (Exception e) {
            setBackgroundColor(getResources().getColor(R.color.grayfont));
        }
        textView.setText(tag);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeCallback != null) {
                    closeCallback.onCloseCallback();
                }
            }
        });
    }

    public interface CloseCallback {
        void onCloseCallback();
    }
}