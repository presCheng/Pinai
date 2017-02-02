package com.jl.atys;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import hrb.jl.pinai.R;

/**
 * Created by Administrator on 2015/7/5 0005.
 */
public class AtyGuide2 extends  AtyBaseGuide{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_guide2);
    }





    @Override
    public void showNext() {
        Intent intent = new Intent(this,AtyGuide3.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity(intent);后面执行；
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this,AtyGuide1.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }
}
