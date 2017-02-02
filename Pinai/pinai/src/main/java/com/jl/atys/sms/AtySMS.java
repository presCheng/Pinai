package com.jl.atys.sms;

import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：消息主页面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-10
 * 下午1:40:28
 * 修改备注：
 *
 * @version 1.0.0
 */
@SuppressWarnings("deprecation")
public class AtySMS extends AtySupport {
    List<View> listViews;
    @SuppressWarnings("deprecation")
    LocalActivityManager manager = null;
    TabHost tabHost = null;
    private ViewPager pager = null;
    // ===========================================
    private TextView unread_laixin;// 好友来信未读小圆圈
    private TextView unread_sms;// 系统来信未读小圆圈
    private BroadcastReceiver systemNotificationsroadcastReceiver = new BroadcastReceiver() {
        /**
         *
         * 未读系统消息广播
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS)) {
                int count = (Integer) intent.getExtras().get("count");
                if (count > 0) {
                    unread_sms.setVisibility(View.VISIBLE);
                    unread_sms.setText(String.valueOf(count));
                } else {
                    unread_sms.setVisibility(View.INVISIBLE);
                }
            }
        }
    };
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        /**
         *
         * 未读好友消息广播
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Config.RECEIVER_REFRESH_UNREAD_MSG)) {
                int count = (Integer) intent.getExtras().get("count");
                if (count > 0) {
                    unread_laixin.setVisibility(View.VISIBLE);
                    unread_laixin.setText(String.valueOf(count));
                } else {
                    unread_laixin.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_sms);
        //变色龙
//        setStatusBarTint(AtySMS.this, getResources().getColor(R.color.background));
        initView(savedInstanceState);
        reflesh();
        filterSystemNotifications();
    }

    /**
     * 监听未读好友发送消息
     *
     * @since 1.0.0
     */
    private void reflesh() {
        IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction(Config.RECEIVER_REFRESH_UNREAD_MSG);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    /**
     * 监听系统的发来的消息
     */
    private void filterSystemNotifications() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS);
        registerReceiver(systemNotificationsroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(systemNotificationsroadcastReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(mRefreshBroadcastReceiver);
        } catch (Exception e) {
        }

    }

    private void initView(Bundle savedInstanceState) {
        manager = new LocalActivityManager(getParent(), true);
        manager.dispatchCreate(savedInstanceState);
        tabHost = (TabHost) findViewById(R.id.th_sms);
        tabHost.setup();
        tabHost.setup(manager);
        pager = (ViewPager) findViewById(R.id.vp_sms);
        listViews = new ArrayList<View>();
        Intent i1 = new Intent(context, AtyChatAll.class);
        listViews.add(getView("AtyChatAll", i1));
        Intent i2 = new Intent(context, AtyContactAll.class);
        listViews.add(getView("AtyContactAll", i2));
        Intent i3 = new Intent(context, AtySMSInBox.class);
        listViews.add(getView("AtySMSInBox", i3));
        RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.aty_sms_tabwiget, null);
        TextView tvTab1 = (TextView) tabIndicator1.findViewById(R.id.tv_title);
        unread_laixin = (TextView) tabIndicator1.findViewById(R.id.tv_unread);
        unread_laixin.setVisibility(View.INVISIBLE);
        tvTab1.setText("面试中");
        RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.aty_sms_tabwiget, null);
        TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tv_title);
        // 追我的人未读小圆圈
        TextView unread_zuiwo = (TextView) tabIndicator2.findViewById(R.id.tv_unread);
        unread_zuiwo.setVisibility(View.INVISIBLE);
        tvTab2.setText("面试列表");
        RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.aty_sms_tabwiget, null);
        TextView tvTab3 = (TextView) tabIndicator3.findViewById(R.id.tv_title);
        // 系统消息未读小圆圈
        unread_sms = (TextView) tabIndicator3.findViewById(R.id.tv_unread);
        int unSysRead = Config.getCacheSysUnRead(context);
        if (unSysRead > 0) {
            unread_sms.setText(unSysRead + "");
        } else {
            unread_sms.setVisibility(View.INVISIBLE);
        }
        tvTab3.setText("消息");
        tabHost.addTab(tabHost.newTabSpec("聊天").setIndicator(tabIndicator1)
                .setContent(i1));
        tabHost.addTab(tabHost.newTabSpec("心动的人").setIndicator(tabIndicator2)
                .setContent(i2));
        tabHost.addTab(tabHost.newTabSpec("消息").setIndicator(tabIndicator3)
                .setContent(i3));
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                tabHost.setOnTabChangedListener(new OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String tabId) {
                        if ("聊天".equals(tabId)) {
                            pager.setCurrentItem(0);
                        }
                        if ("心动的人".equals(tabId)) {
                            pager.setCurrentItem(1);
                        }
                        if ("消息".equals(tabId)) {
                            pager.setCurrentItem(2);
                        }
                    }
                });
            }
        });
        pager.setAdapter(new MyPageAdapter(listViews));
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 把Activity转换成一个view
     *
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return getParent().onKeyDown(keyCode, event);
    }

    private class MyPageAdapter extends PagerAdapter {
        private List<View> list;

        private MyPageAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object arg2) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
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

    }
}
