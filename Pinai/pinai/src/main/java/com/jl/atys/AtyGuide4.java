package com.jl.atys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jl.basic.AtySupport;

import hrb.jl.pinai.R;

/**
 * 类描述：导航界面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyGuide4 extends AtyBaseGuide {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_guide);

        Button btn_regist = (Button) findViewById(R.id.guide_btn_regist);
        btn_regist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //initSMSDK();
                //registerUser("","");
                startActivity(new Intent(context, AtySendSms.class));
                finish();
            }

        });
        Button btn_login = (Button) findViewById(R.id.guide_btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(AtyGuide4.this,
                        AtyLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void showNext() {

    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,AtyGuide3.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }
}
