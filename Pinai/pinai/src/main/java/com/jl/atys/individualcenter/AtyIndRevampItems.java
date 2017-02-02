package com.jl.atys.individualcenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jl.basic.AtySupport;

import hrb.jl.pinai.R;

/**
 * 类描述：个人信息查看
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-23 上午10:02:49
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyIndRevampItems extends AtySupport {

    private EditText editText;//内容天填写框
    private TextView itemChar;//剩余文字框
    private int count;//可以输入多少个字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_revamp_item);
        String tag = (String) getIntent().getExtras().get("tag");
        String content = (String) getIntent().getExtras().get("content");
        count = (Integer) getIntent().getExtras().get("count");
        ((TextView) findViewById(R.id.ind_rap_item_tv)).setText(tag);
        editText = (EditText) findViewById(R.id.ind_rap_item_et);
        editText.setText(content);

        // editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
        //-----------------------------设置高度
        ViewGroup.LayoutParams lp = editText.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (count == 50) {
            lp.height = 100;
        } else {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        editText.requestLayout();
        //------------------------------------
        itemChar = (TextView) findViewById(R.id.ind_rap_item_char);
        itemChar.setText(0 + "/" + count);
        editText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = editText.getSelectionStart();
                selectionEnd = editText.getSelectionEnd();
                if (temp.length() > count) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    editText.setText(s);
                    editText.setSelection(tempSelection);
                    itemChar.setText(tempSelection + "/" + count);
                    itemChar.setTextColor(getResources().getColor(R.color.background_pink));
                } else {
                    itemChar.setText(selectionStart + "/" + count);
                    itemChar.setTextColor(getResources().getColor(R.color.grayfont));
                    if (temp.length() == count) {
                        itemChar.setTextColor(getResources().getColor(R.color.background_pink));
                    }
                }
            }
        });
        findViewById(R.id.ind_rap_item_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ((EditText) findViewById(R.id.ind_rap_item_et)).getText().toString();
                Intent i = new Intent(AtyIndRevampItems.this, AtyIndRevamp.class);
                i.putExtra("content", content);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }


    public void back(View v) {
        finish();
    }
}
