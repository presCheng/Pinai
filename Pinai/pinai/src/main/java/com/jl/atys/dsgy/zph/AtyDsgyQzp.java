package com.jl.atys.dsgy.zph;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.net.PostLikeJob;

import hrb.jl.pinai.R;

public class AtyDsgyQzp extends AtySupport {
    private String title;
    private String content;
    private String rule1;
    private String rule2;
    private String rule3;
    private String rule4;
    private String rule5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dsgy_qzp);
        setStatusBarTint(AtyDsgyQzp.this, getResources().getColor(R.color.background_pink));

    }

    private void initView() {
        title = ((EditText) findViewById(R.id.title)).getText().toString();
        content = ((EditText) findViewById(R.id.content)).getText().toString();
        rule1 = ((EditText) findViewById(R.id.rule_1)).getText().toString();
        rule2 = ((EditText) findViewById(R.id.rule_2)).getText().toString();
        rule3 = ((EditText) findViewById(R.id.rule_3)).getText().toString();
        rule4 = ((EditText) findViewById(R.id.rule_4)).getText().toString();
        rule5 = ((EditText) findViewById(R.id.rule_5)).getText().toString();
    }
    public void postNews(View v) {
        initView();
        if (TextUtils.isEmpty(title)||TextUtils.isEmpty(content)||TextUtils.isEmpty(rule1)||TextUtils.isEmpty(rule2)){
            showToast("请填写必要信息哟~");
        }else{
            showProgressDialog("提示","发送中...",false);
            new PostLikeJob(Config.getCacheID(context), title, content, rule1, rule2, rule3, rule4, rule5, new PostLikeJob.SuccessCallback() {
                @Override
                public void onSuccess() {
                    closeProgressDialog();
                    showToast("发帖成功");
                    finish();
                }
            }, new PostLikeJob.FailCallback() {
                @Override
                public void onFail(String error) {
                    closeProgressDialog();
                    showToast("发帖失败");
                }
            });
        }
    }
    public void back(View v) {
        finish();
    }
}
