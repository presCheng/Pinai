package com.jl.atys.individualcenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.jl.atys.AtyLogin;
import com.jl.atys.chat.utils.PreferenceUtils;
import com.jl.basic.AtySupport;
import com.jl.customs.ConfirmAlertDialog;

import hrb.jl.pinai.R;

/**
 * 类描述：基本设置界面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-2-8
 * 下午2:14:01
 * 修改备注：
 *
 * @version 1.0.0
 */

public class AtyIndSetting extends AtySupport implements CompoundButton.OnCheckedChangeListener {
    ToggleButton toggle1, toggle2, toggle3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_setting);
        toggle1 = (ToggleButton) findViewById(R.id.toggle1);
        toggle2 = (ToggleButton) findViewById(R.id.toggle2);
        toggle3 = (ToggleButton) findViewById(R.id.toggle3);
        if (PreferenceUtils.getInstance(context).getSettingMsgVibrate()) {
            toggle1.setChecked(true);
        } else {
            toggle1.setChecked(false);

        }
        if (PreferenceUtils.getInstance(context).getSettingMsgSound()) {
            toggle2.setChecked(true);
        } else {
            toggle2.setChecked(false);

        }
        if (PreferenceUtils.getInstance(context).getSettingMsgSpeaker()) {
            toggle3.setChecked(true);
        } else {
            toggle3.setChecked(false);

        }
        toggle1.setOnCheckedChangeListener(this);
        toggle2.setOnCheckedChangeListener(this);
        toggle3.setOnCheckedChangeListener(this);
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        switch (buttonView.getId()) {
            case R.id.toggle1:
                // 设置收到消息是否震动 默认为true
                PreferenceUtils.getInstance(context).setSettingMsgVibrate(isChecked);
                options.setNoticedByVibrate(PreferenceUtils.getInstance(context).getSettingMsgVibrate());
                Log.e("Tag", isChecked + "");
                break;
            case R.id.toggle2:
                PreferenceUtils.getInstance(context).setSettingMsgSound(isChecked);
                options.setNoticeBySound(PreferenceUtils.getInstance(context).getSettingMsgSound());

                Log.e("Tag", isChecked + "");
                break;
            case R.id.toggle3:
                PreferenceUtils.getInstance(context).setSettingMsgSpeaker(isChecked);
                // 设置语音消息播放是否设置为扬声器播放 默认为true
                options.setUseSpeaker(PreferenceUtils.getInstance(context).getSettingMsgSpeaker());
                Log.e("Tag", isChecked + "");
                break;
        }
    }


    public void exit(View v){
        //退出
        ConfirmAlertDialog.Builder builder = new ConfirmAlertDialog.Builder(
                context);
        builder.setTitle("提示").setContent("确认退出吗？").setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        pinApplication.logout();
                        startActivity(new Intent(AtyIndSetting.this, AtyLogin.class));
                        finish();
                        dialog.cancel();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}


