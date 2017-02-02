package com.jl.atys.dsgy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jl.atys.AtyMain;
import com.jl.atys.dsgy.jly.AtyDsgyJlY;
import com.jl.atys.dsgy.ssph.AtyDsgySsph;
import com.jl.atys.dsgy.zph.AtyDsgyZph;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.domain.OpenArticleBean;
import com.jl.net.GetForumUnread;
import com.jl.net.GetOpenArticles;
import com.jl.utils.UserTools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hrb.jl.pinai.R;

public class AtyDsgy extends AtySupport {


    public TextView unRead;//未读消息
    /**
     * 滑动速度设置
     */
    FixedSpeedScroller mScroller = null;
    //private ViewGroup mViewGroup = null;
    private ViewPager mViewPager = null;
    // 包含圆点图片的View
    private ViewGroup mImageCircleView = null;
    // 数据解析类
    //private NewsXmlParser mParser = null;
    private String u;
    //轮播图标题
    //private TextView title;
    /**
     * 每五秒钟更改一次轮播图
     */
    private Timer timer;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int count = mViewPager.getAdapter().getCount();
            int current = mViewPager.getCurrentItem();
            // Log.e("mViewPager.getAdapter().getCount()",count+"");
            //Log.e("mViewPager.getCurrentItem()",current+"");
            int num;
            num = current + 1;
            if (num == count) {
                num = 0;
            }
            mViewPager.setCurrentItem(num);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_dsgy);
        //变色龙
        //setStatusBarTint(AtyDsgy.this, getResources().getColor(R.color.background_pink));
        AtyMain parent = (AtyMain) getParent();
        // 放到父类中，可以回调
        parent.putChildDsgy(this);
        init();
        initView();
        u = Config.getCacheID(context);

    }

    private void init() {
        //爱诊所
        findViewById(R.id.adsy_azs).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AtyDsgy.this, AtyDsgyZph.class);
                startActivity(i);
            }
        });

        findViewById(R.id.adsy_jly).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AtyDsgy.this, AtyDsgyForum.class);
                //这里因为直接在atyDsgyBBs获得id 会报错 bug未知
                i.putExtra("userId", u);
                i.putExtra("categoryId", Config.KEY_CAT_AZS);
                startActivity(i);
            }
        });

        findViewById(R.id.adsy_ssph).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AtyDsgy.this, AtyDsgySsph.class);
                startActivity(i);
            }
        });
        findViewById(R.id.adsy_zph).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(AtyDsgy.this, AtyDsgyJlY.class);
                startActivity(i);
            }
        });
        //男人帮
//        findViewById(R.id.adsy_nrb).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(AtyDsgy.this, AtyDsgyForum.class);
//                //这里因为直接在atyDsgyBBs获得id 会报错 bug未知
//                i.putExtra("userId", u);
//                i.putExtra("categoryId", Config.KEY_CAT_NRB);
//                startActivity(i);
//            }
//        });
//        //女人窝
//        findViewById(R.id.adsy_nrw).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(AtyDsgy.this, AtyDsgyForum.class);
//                //这里因为直接在atyDsgyBBs获得id 会报错 bug未知
//                i.putExtra("userId", u);
//                i.putExtra("categoryId", Config.KEY_CAT_NRW);
//                startActivity(i);
//            }
//        });
        unRead = (TextView) findViewById(R.id.dsgy_un_read);
        unRead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unRead.setText(getTwoColorText("0", "条未读消息"));
                //unRead.setVisibility(View.VISIBLE);
                startActivity(new Intent(AtyDsgy.this, AtyDsgyUnRead.class));
            }
        });

        new GetForumUnread(Config.getCacheID(context), new GetForumUnread.SuccessCallback() {
            @Override
            public void onSuccess(String num) {
                //TODO 未读消息没有显示界面
//                if (!num.equals("0")){
//                    unRead.setVisibility(View.VISIBLE);
//                }
                unRead.setText(getTwoColorText(num, "条未读消息"));
            }
        }, new GetForumUnread.FailCallback() {
            @Override
            public void onFail() {

            }
        });
        ImageView head = (ImageView) findViewById(R.id.ind_head);
        UserTools.displayImage(Config.getCachePortrait(context),head,getOptions());

    }

    private SpannableStringBuilder getTwoColorText(String str1, String str2) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1 + " " + str2);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_pink)), 0, str1.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 初始化轮播图
     */
    private void initView() {
       // title  = (TextView) findViewById(R.id.dsgy_title);
        mViewPager = (ViewPager) findViewById(R.id.dsgy_iv_slide_page);
        mImageCircleView = (ViewGroup) findViewById(R.id.dsgy_layout_circle_images);
        new GetOpenArticles(new GetOpenArticles.SuccessCallback() {
            @Override
            public void onSuccess(List<OpenArticleBean> articles) {
                ArrayList<View> mImagePageViewList = new ArrayList<View>();
                ImageView[] mImageCircleViews = new ImageView[articles.size()];
                SlideImageLayout mSlideLayout = new SlideImageLayout(AtyDsgy.this);
                mSlideLayout.setCircleImageLayout(articles.size());
                for (int i = 0; i < articles.size(); i++) {
                    String thumBnail = articles.get(i).getThumbnails();
                    String url = articles.get(i).getUrl();
                    mImagePageViewList.add(mSlideLayout.getSlideImageLayout(thumBnail, url));
                    mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
                    //把圆点加入
                    mImageCircleView.addView(mSlideLayout.getLinearLayout(
                            mImageCircleViews[i], 30, 30));
                }

                //第一个标题
                //title.setText(articles.get(0).getTitle());
                // 设置ViewPager
                mViewPager.setAdapter(new SlideImageAdapter(mImagePageViewList));
                mViewPager.setOnPageChangeListener(new ImagePageChangeListener(mImageCircleViews, mSlideLayout));
                scheduleTimer();
                controlViewPagerSpeed();


            }
        }, new GetOpenArticles.FailCallback() {
            @Override
            public void onFail() {
            }
        });

    }

    private void scheduleTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 0, 5000);
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }

    private void controlViewPagerSpeed() {
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(
                    context,
                    new AccelerateInterpolator());
            mScroller.setmDuration(1000); // 2000ms
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return getParent().onKeyDown(keyCode, event);
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

        private ImagePageChangeListener(ImageView[] mImageCircleViews, SlideImageLayout mSlideLayout) {
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
            //title.setText(articles.get(index).getTitle());
            for (int i = 0; i < mImageCircleViews.length; i++) {
                mImageCircleViews[index]
                        .setBackgroundResource(R.drawable.dot_selected);
                if (index != i) {
                    mImageCircleViews[i]
                            .setBackgroundResource(R.drawable.dot_none);
                }
            }
        }
    }
}
