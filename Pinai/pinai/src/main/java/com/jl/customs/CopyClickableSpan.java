package com.jl.customs;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import hrb.jl.pinai.R;

/**
 * 自定义textview点击事件
 */
public class CopyClickableSpan  extends ClickableSpan{

    String string;
    Context context;
    public CopyClickableSpan(String str,Context context){
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
        if (Build.VERSION.SDK_INT>=11) {
            ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setText(string); // 复制
            Toast.makeText(context,"复制到剪切板",Toast.LENGTH_SHORT).show();
        }
    }
}
