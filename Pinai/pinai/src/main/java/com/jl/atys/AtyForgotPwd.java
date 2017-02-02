package com.jl.atys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.net.RecoveryPassword;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import hrb.jl.pinai.R;

public class AtyForgotPwd extends AtySupport implements OnClickListener {

    public String phString;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    showToast("验证码输入正确,请重置密码");
                    pwd.setVisibility(View.VISIBLE);
                    rpwd.setVisibility(View.VISIBLE);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    showToast("验证码已经发送");
                }
            } else {
                showToast("请输入正确验证码");
                ((Throwable) data).printStackTrace();
            }
        }
    };
    private Button sensmsButton;
    private EditText phonEditText, verEditText;
    private EditText pwd, rpwd;//密码
    private Timer timer;
    private Handler handlerTimer = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                sensmsButton.setEnabled(true);
                sensmsButton.setTextColor(context.getResources().getColor(
                        R.color.white));
                sensmsButton.setText("获取验证码");
                timer.cancel();
            } else {
                sensmsButton.setText(msg.what + "秒");
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_forgot_pwd);
        sensmsButton = (Button) findViewById(R.id.button1);
        Button submit = (Button) findViewById(R.id.button2);
        phonEditText = (EditText) findViewById(R.id.editText1);
        verEditText = (EditText) findViewById(R.id.editText2);
        sensmsButton.setOnClickListener(this);
        submit.setOnClickListener(this);
        //输入四位验证码正确才行
        verEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    //verEditText.setEnabled(false);
                    //校验验证码
                    SMSSDK.submitVerificationCode("86", phString, verEditText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pwd = (EditText) findViewById(R.id.new_pwd);
        rpwd = (EditText) findViewById(R.id.confirm_new_pwd);
        SMSSDK.initSDK(this, Config.APPKEY, Config.APPSECRET);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    public void toLogin(View v) {
        startActivity(new Intent(AtyForgotPwd.this, AtyLogin.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1://获取验证码
                if (!TextUtils.isEmpty(phonEditText.getText().toString())) {
                    SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
                    phString = phonEditText.getText().toString();
                    phonEditText.setEnabled(false);
                    sensmsButton.setEnabled(false);
                    sensmsButton.setTextColor(context.getResources().getColor(
                            R.color.gray_normal));
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        /**
                         * 验证的时间
                         */
                        private int countTime = 60;

                        @Override
                        public void run() {
                            handlerTimer.sendEmptyMessage(countTime--);
                            if (countTime == 0) {
                                handlerTimer.sendEmptyMessage(0);
                            }
                            Log.e("E", countTime + "");
                        }
                    }, 0, 1000);
                } else {
                    showToast("电话不能为空");
                }
                break;
            case R.id.button2://提交修改信息
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        showProgressDialog(context, "", "请稍候....", false);
        String strPwd = pwd.getText().toString();
        String strRpwd = rpwd.getText().toString();
        if (TextUtils.isEmpty(strPwd) || TextUtils.isEmpty(strRpwd)) {
            closeProgressDialog();
            showToast("密码不能为空");
            return;
        }
        if (!strPwd.equals(strRpwd)) {
            closeProgressDialog();
            showToast("密码不一致");
            return;
        }
        String phone = phonEditText.getText().toString();
        new RecoveryPassword(phone, strPwd, new RecoveryPassword.SuccessCallback() {
            @Override
            public void onSuccess() {
                showToast("修改成功");
                startActivity(new Intent(AtyForgotPwd.this, AtyLogin.class));
                closeProgressDialog();
                finish();
            }
        }, new RecoveryPassword.FailCallback() {
            @Override
            public void onFail(String error) {
                closeProgressDialog();
                showToast(error);
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
