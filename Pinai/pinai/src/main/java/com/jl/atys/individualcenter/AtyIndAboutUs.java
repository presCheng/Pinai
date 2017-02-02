package com.jl.atys.individualcenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.CopyClickableSpan;
import com.jl.customs.ToCallClickableSpan;
import com.umeng.update.UmengUpdateAgent;

import hrb.jl.pinai.R;

/**
 * 类描述：关于我们
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-30 下午2:14:01
 * 修改备注：
 *
 * @version 1.0.0
 */

public class AtyIndAboutUs extends AtySupport {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_about_us);
        findViewById(R.id.about_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AtyIndAboutUs.this, AtyIndService.class).putExtra("url", Config.SERVER_SERVICE_URL));
            }
        });
        findViewById(R.id.about_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.forceUpdate(context);
            }
        });
        initText();
    }

    private void initText() {
        TextView textView = (TextView) findViewById(R.id.about_intro_2);
        String QQ ="QQ:523591643";
        SpannableString spanQQ =new SpannableString(QQ);
        CopyClickableSpan cCSPanQQ =new CopyClickableSpan(QQ,this);
        spanQQ.setSpan(cCSPanQQ,3,QQ.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        String tel ="Tel:15636129303";
        SpannableString spanTel =new SpannableString(tel);
        ToCallClickableSpan tCCSpan = new ToCallClickableSpan(tel,this);
        spanTel.setSpan(tCCSpan, 4, tel.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        String email ="Mail:support@pinai521.com";
        SpannableString spanEmail =new SpannableString(email);
        CopyClickableSpan cCSPanEmail =new CopyClickableSpan(email,this);
        spanEmail.setSpan(cCSPanEmail,5,email.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        textView.append(spanQQ);
        textView.append("\n\n");
        textView.append(spanTel);
        textView.append("\n\n");
        textView.append(spanEmail);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void back(View v) {
        finish();
    }
}
