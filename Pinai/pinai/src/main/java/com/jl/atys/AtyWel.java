package com.jl.atys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.jl.basic.AtySupport;
import com.jl.basic.PinApplication;
import com.umeng.analytics.MobclickAgent;

import hrb.jl.pinai.R;

/**
 * 类描述：欢迎界面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyWel extends AtySupport {
    private static final int sleepTime = 2500;
    Intent intent;
    // 设置配置信息
    String PREFS_NAME = "pinai";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_wel);
        MobclickAgent.updateOnlineConfig(context);
        init();
    }

    /**
     * Function: 初始化信息
     */
    private void init() {

        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        // 设置时间
        animation.setDuration(1000);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFirst()) {
                    Log.i("qqq", "第一次进入");
                    // 第一次进入引导界面
                    intent = new Intent(AtyWel.this, AtyGuide1.class);
                    startActivity(intent);
                } else {
                    Log.i("qqq", "不是第一次进入");
                    initChat();
                }
                // 结束本activity进程
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) this
                .findViewById(R.id.welcome);
        relativeLayout.setAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // initChat();
    }

    private void initChat() {
        if (PinApplication.getInstance().getUserName() != null
                && PinApplication.getInstance().getPassword() != null) {
            // ** 免登陆情况 加载所有本地群和回话
            // ** manually load all local groups and conversations in case we
            // are auto login
            long start = System.currentTimeMillis();
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
            long costTime = System.currentTimeMillis() - start;
            // 等待sleeptime时长
            if (sleepTime - costTime > 0) {
                try {
                    Thread.sleep(sleepTime - costTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 进入主页面
            startActivity(new Intent(AtyWel.this, AtyMain.class));
            finish();
        } else {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
            startActivity(new Intent(AtyWel.this, AtyLogin.class));
            finish();
        }
    }

    /**
     * 这个方法是判断是不是第一次安装本程序，
     * 如果是第一次的话， 以后就不用在把应用中的数据库导入文件夹中了
     *
     * @return boolean
     * @since 1.0.0
     */
    private boolean isFirst() {
        settings = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (settings.getBoolean("isFirst", true)) {
            editor = settings.edit();
            editor.putBoolean("isFirst", false);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }


}
