package com.jl.atys.individualcenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.jl.atys.gopin.GoPinSchool;
import com.jl.basic.AtySupport;
import com.jl.db.SqliteUtils;
import com.jl.utils.UserTools;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：学校选择（半透明）
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 上午11:54:11
 * 修改备注：
 *
 * @version 1.0.0
 */

public class AtySchoolChoose extends AtySupport {
    private SchoolChooseAdapter gAdapter;
    private ListView gLv;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_school_choose);
        search = (EditText) findViewById(R.id.gopin_school_search);
        gLv = (ListView) findViewById(R.id.gopin_lv_search);
        gAdapter = new SchoolChooseAdapter(context);
        gLv.setAdapter(gAdapter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String name = s.toString();
                if (!TextUtils.isEmpty(name)) {
                    boolean b = UserTools.checkNameChese(name);
                    if (b) {
                        List<GoPinSchool> data = SqliteUtils.getInstance()
                                .getSchool(context, name);
                        gAdapter.clear();
                        gAdapter.addAll(data);
                    }
                    //=============
                    else {
                        List<GoPinSchool> data = SqliteUtils.getInstance()
                                .getSchool(context, "哈");
                        gAdapter.clear();
                        gAdapter.addAll(data);
                    }
                    //=================
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = gAdapter.getItem(position).getName();
                //ComponentName s =getIntent().getComponent();
                Intent ai = new Intent(AtySchoolChoose.this, AtyIndRevamp.class);
                ai.putExtra("content", name);
                setResult(RESULT_OK, ai);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ai = new Intent(AtySchoolChoose.this, AtyIndRevamp.class);
            setResult(RESULT_OK, ai);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
