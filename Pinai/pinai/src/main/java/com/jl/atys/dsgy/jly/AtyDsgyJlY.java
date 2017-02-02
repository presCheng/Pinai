package com.jl.atys.dsgy.jly;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.net.MatchUser;
import com.jl.net.MatchUserRecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

public class AtyDsgyJlY extends AtySupport {
    // private ViewGroup mViewGroup = null;
    private ViewPager mViewPager = null;
    // 包含圆点图片的View
    private ViewGroup mImageCircleView = null;
    // 滑动速度设置

    FixedSpeedScroller mScroller = null;

    private Button btnFilpCard;
    //是否翻牌
    private boolean[] isFilps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dsgy_jly);
        setStatusBarTint(AtyDsgyJlY.this, getResources().getColor(R.color.background_pink));

        showProgressDialog("", "加载中", false);

        mViewPager = (ViewPager) findViewById(R.id.dsgy_iv_slide_page);
        mImageCircleView = (ViewGroup) findViewById(R.id.dsgy_layout_circle_images);

        getUsers();
    }

    private ArrayList<View> mImagePageViewList;

    /**
     * 初始化轮播图
     */
    private void initView(final List<JlyBean> jlyBeans, final String match) {

        mViewPager = (ViewPager) findViewById(R.id.dsgy_iv_slide_page);
        mImageCircleView = (ViewGroup) findViewById(R.id.dsgy_layout_circle_images);
        mImagePageViewList = new ArrayList<>();
        int imageCount = jlyBeans.size();
        isFilps = new boolean[imageCount];
        ImageView[] mImageCircleViews = new ImageView[imageCount];
        SlideImageLayout mSlideLayout = new SlideImageLayout(AtyDsgyJlY.this);
        mSlideLayout.setCircleImageLayout(imageCount);
        for (int i = 0; i < imageCount; i++) {
            mImagePageViewList.add(mSlideLayout.getSlideImageLayout(
                    AtyDsgyJlY.this, jlyBeans.get(i)));
            mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
            // 把圆点加入
            mImageCircleView.addView(mSlideLayout.getLinearLayout(
                    mImageCircleViews[i], 10, 10));
        }
        // 设置ViewPager
        mViewPager.setAdapter(new SlideImageAdapter(mImagePageViewList));
        mViewPager.setOnPageChangeListener(new ImagePageChangeListener(
                mImageCircleViews, mSlideLayout));
        controlViewPagerSpeed();
        btnFilpCard = (Button) findViewById(R.id.btn_filp_card);
        final TextView matchc = (TextView) findViewById(R.id.match_ci);
        int matchNum =Integer.parseInt(match);
        matchc.setText("今天还可以剩下 "+(3- matchNum)+" 次翻牌机会");
        btnFilpCard.setOnClickListener(new OnClickListener() {
            int matchNum =Integer.parseInt(match);
            @Override
            public void onClick(View v) {
                if (matchNum==3){
                    btnFilpCard.setClickable(false);
                    return;
                }
                if (!isFilps[currentNum]) {
                    matchNum++;
                    matchc.setText("今天还可以剩下 " +(3- matchNum) + " 次翻牌机会");
                    applyRotation(mImagePageViewList.get(currentNum), 0, 0, 90);
                    isFilps[currentNum] = true;
                    clickUserId += jlyBeans.get(currentNum).getId() + ",";
                }
            }
        });
    }
    String clickUserId ="";
    private void getUsers() {
        new MatchUser(Config.getCacheID(context), new MatchUser.SuccessCallback() {
            @Override
            public void onSuccess(List<JlyBean> jlyBeans,String match) {
                initView(jlyBeans,match);
                closeProgressDialog();
            }
        }, new MatchUser.FailCallback() {
            @Override
            public void onFail(String error) {
                if (error.equals("3")){
                    showToast("今天次数用完了");
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(clickUserId)) {
            clickUserId = clickUserId.substring(0, clickUserId.length() - 1);
        }
        new MatchUserRecord(Config.getCacheID(context), clickUserId, new MatchUserRecord.SuccessCallback() {
            @Override
            public void onSuccess() {
                System.out.print("MatchUserRecord");
            }
        }, new MatchUserRecord.FailCallback() {
            @Override
            public void onFail(String error) {
                System.out.print("MatchUserRecord error");
            }
        });
        System.out.print("onDestroy");
        super.onDestroy();

    }

    private void controlViewPagerSpeed() {
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(AtyDsgyJlY.this,
                    new AccelerateInterpolator());
            mScroller.setmDuration(500); // 2000ms
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 滑动图片数据适配器
     */

    private class SlideImageAdapter extends PagerAdapter {
        private ArrayList<View> mImagePageViewList;

        private SlideImageAdapter(ArrayList<View> mImagePageViewList) {
            this.mImagePageViewList = mImagePageViewList;
        }

        @Override
        public int getCount() {
            return mImagePageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View view, int arg1, Object arg2) {
            ((ViewPager) view).removeView(mImagePageViewList.get(arg1));
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(mImagePageViewList.get(position));

            return mImagePageViewList.get(position);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }

    /**
     * 滑动页面更改事件监听器
     */
    private class ImagePageChangeListener implements OnPageChangeListener {
        private ImageView[] mImageCircleViews;
        private SlideImageLayout mSlideLayout;

        private ImagePageChangeListener(ImageView[] mImageCircleViews,
                                        SlideImageLayout mSlideLayout) {
            this.mImageCircleViews = mImageCircleViews;
            this.mSlideLayout = mSlideLayout;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            mSlideLayout.setPageIndex(index);
            currentNum = index;
            // title.setText(articles.get(index).getTitle());
            for (int i = 0; i < mImageCircleViews.length; i++) {
                mImageCircleViews[index]
                        .setBackgroundResource(R.drawable.dot_jly_press);
                if (index != i) {
                    mImageCircleViews[i]
                            .setBackgroundResource(R.drawable.dot_jly);
                }
            }
        }
    }

    private int currentNum = 0;

    // =====================

    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the
     *                 list
     * @param start    the start angle at which the rotation must begin
     * @param end      the end angle of the rotation
     */
    private void applyRotation(View mContainer, int position, float start,
                               float end) {
        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final RotateAnimation rotation = new RotateAnimation(start, end,
                centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        RelativeLayout rl_layout01= (RelativeLayout) mContainer.findViewById(R.id.rl_layout01);
        rotation.setAnimationListener(new DisplayNextView(mContainer,rl_layout01, position));
        mContainer.findViewById(R.id.rl_layout01).startAnimation(rotation);

    }

    /**
     * This class listens for the end of the first half of the animation. It
     * then posts a new action that effectively swaps the views when the
     * container is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;
        private RelativeLayout rl_layout01;
        private View mContainer;
        private DisplayNextView(View mContainer,RelativeLayout rl_layout01, int position) {
            mPosition = position;
            this.mContainer =mContainer;
           this.rl_layout01=rl_layout01;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            rl_layout01.post(new SwapViews(mContainer,rl_layout01, mPosition));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;
        private RelativeLayout rl_layout01;
        private RelativeLayout rl_layout02;
        private View mContainer;
        public SwapViews(View mContainer,RelativeLayout rl_layout01, int position) {
            mPosition = position;
            this.rl_layout01 =rl_layout01;
            this.mContainer=mContainer;
            rl_layout02 = (RelativeLayout) mContainer
                    .findViewById(R.id.rl_layout02);
        }

        public void run() {
            final float centerX = rl_layout01.getWidth() / 2.0f;
            final float centerY = rl_layout01.getHeight() / 2.0f;
            RotateAnimation rotation;
            if (mPosition > -1) {
                rl_layout01.setVisibility(View.GONE);
                rl_layout02.setVisibility(View.VISIBLE);
                rotation = new RotateAnimation(90, 180, centerX, centerY,
                        310.0f, false);
            } else {
                rl_layout02.setVisibility(View.GONE);
                rl_layout01.setVisibility(View.VISIBLE);
                rotation = new RotateAnimation(90, 0, centerX, centerY, 310.0f,
                        false);
            }
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            rl_layout01.startAnimation(rotation);
        }
    }

    public void back(View v) {
        finish();
    }
}
