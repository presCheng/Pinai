package com.jl.customs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import hrb.jl.pinai.R;

/**
 * 自定义textview 拨打电话事件
 */
public class ToCallClickableSpan extends ClickableSpan{

    String string;
    Context context;
    public ToCallClickableSpan(String str, Context context){
        super();
        this.string = str;
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(context.getResources().getColor(R.color.background_pink));
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + string));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.d("Tag","okok");
    }
}
