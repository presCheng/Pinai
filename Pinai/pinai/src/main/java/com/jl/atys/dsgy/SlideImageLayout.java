package com.jl.atys.dsgy;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.jl.atys.individualcenter.AtyIndService;
import com.jl.basic.AtySupport;
import com.jl.utils.UserTools;

import java.util.ArrayList;

import hrb.jl.pinai.R;


public class SlideImageLayout {
    // 包含图片的ArrayList
    private ArrayList<ImageView> mImageList = null;
    private AtySupport mContext = null;
    // 圆点图片集合
    private ImageView[] mImageViews = null;
    // 表示当前滑动图片的索引
    private int pageIndex = 0;

    public SlideImageLayout(AtySupport context) {
        this.mContext = context;
        mImageList = new ArrayList<ImageView>();
    }

    /**
     * 生成滑动图片区域布局
     *
     * @param thumbnail
     * @return
     */
    public View getSlideImageLayout(String thumbnail, String url) {
        // 包含TextView的LinearLayout
        LinearLayout imageLinerLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1);
        ImageView iv = new ImageView(mContext);
        iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //根据id 来设置图片
        int width = mContext.getWindowManager().getDefaultDisplay().getWidth();
        Log.e("tag", width + "");
        UserTools.displayImageHasListener(thumbnail, iv, null, LayoutParams.MATCH_PARENT, width);
        //根据url来跳转界面
        iv.setOnClickListener(new ImageOnClickListener(url));
        //iv.setBackgroundColor(mContext.getResources().getColor(R.color.background));
        imageLinerLayout.addView(iv, imageLinerLayoutParames);
        mImageList.add(iv);
        return imageLinerLayout;
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
            mImageViews[index].setBackgroundResource(R.drawable.dot_selected);
        } else {
            mImageViews[index].setBackgroundResource(R.drawable.dot_none);
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

    // 滑动页面点击事件监听器
    private class ImageOnClickListener implements OnClickListener {
        private String url;

        private ImageOnClickListener(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(mContext, AtyIndService.class).putExtra("url", url));
        }
    }
}
