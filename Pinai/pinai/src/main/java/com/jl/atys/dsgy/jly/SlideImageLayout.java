package com.jl.atys.dsgy.jly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.utils.UserTools;

import java.util.ArrayList;

import hrb.jl.pinai.R;


public class SlideImageLayout {
    // 包含图片的ArrayList
    private ArrayList<ImageView> mImageList = null;
    private Activity mContext = null;
    // 圆点图片集合
    private ImageView[] mImageViews = null;
    // 表示当前滑动图片的索引
    private int pageIndex = 0;

    public SlideImageLayout(Activity context) {
        this.mContext = context;
        mImageList = new ArrayList<ImageView>();
    }

    /**
     * 生成滑动图片区域布局
     * 根据thumbnail来生成视图
     * @return View
     */
    public View getSlideImageLayout(final Context context, final JlyBean jlyBean) {
    	 View convertView = LayoutInflater.from(context).inflate(
                 R.layout.card, null);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.match_card);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AtyGoPinProfile.class);
                i.putExtra("receiverId", jlyBean.getId());
                context.startActivity(i);
            }
        });
        UserTools.displayImage(jlyBean.getPortrait(), imageView, null);
        ImageView sex = (ImageView) convertView.findViewById(R.id.sex);
        if (jlyBean.getSex().equals("F")) {
            sex.setBackgroundResource(R.drawable.big_girl);
        } else {
            sex.setBackgroundResource(R.drawable.big_boy);
        }
        TextView nickname = (TextView) convertView.findViewById(R.id.nickname);
        nickname.setText(jlyBean.getNickName());
        TextView grade = (TextView) convertView.findViewById(R.id.grade);
        grade.setText("出生年："+jlyBean.getBorn_year());

        return convertView;
    }

    /**
     * 获取LinearLayout
     *
     * @param view
     * @param width
     * @param height
     * @return
     */
    public View getLinearLayout(View view, int width, int height) {
        LinearLayout linerLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(
                width,
                height,
                1);
        // 这里最好也自定义设置，有兴趣的自己设置。
        linerLayout.setPadding(5, 5, 5, 5);
        linerLayout.addView(view, linerLayoutParames);

        return linerLayout;
    }

    /**
     * 设置圆点个数
     *
     * @param size
     */
    public void setCircleImageLayout(int size) {
        mImageViews = new ImageView[size];
    }

    /**
     * 生成圆点图片区域布局对象
     *
     * @param index
     * @return
     */
    public ImageView getCircleImageLayout(int index) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setLayoutParams(new LayoutParams(10, 10));
        mImageView.setScaleType(ScaleType.FIT_XY);
        mImageViews[index] = mImageView;
        if (index == 0) {
            //默认选中第一张图片
            mImageViews[index].setBackgroundResource(R.drawable.dot_jly);
        } else {
            mImageViews[index].setBackgroundResource(R.drawable.dot_jly_press);
        }

        return mImageViews[index];
    }

    /**
     * 设置当前滑动图片的索引
     *
     * @param index
     */
    public void setPageIndex(int index) {
        pageIndex = index;
    }
}
