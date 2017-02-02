package com.jl.atys.gopin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.jl.atys.chat.domain.OpenUnver;
import com.jl.basic.AtySupport;
import com.jl.net.OpenUniversity;

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

public class AtyGoPinSelect extends AtySupport {
    private SchoolSelectAdapter gAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_gopin_select);
        //变色龙
        setStatusBarTint(AtyGoPinSelect.this, getResources().getColor(R.color.background_pink));

        ListView gLv = (ListView) findViewById(R.id.gopin_lv_search);
        gAdapter = new SchoolSelectAdapter(context);
        new OpenUniversity(new OpenUniversity.SuccessCallback() {
            @Override
            public void onSuccess(List<OpenUnver> datas) {
                findViewById(R.id.playout).setVisibility(View.GONE);

                gAdapter.addAll(datas);
            }
        }, new OpenUniversity.FailCallback() {
            @Override
            public void onFail(String error) {
                findViewById(R.id.playout).setVisibility(View.GONE);
            }
        });
        gLv.setAdapter(gAdapter);
        gLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    Intent ai = new Intent(AtyGoPinSelect.this, AtyGoPin.class);
                    ai.putExtra("name", "");
                    setResult(RESULT_OK, ai);
                    finish();
                    return;
                }
                String status = gAdapter.getItem(position).getStatus();
                if (status.equals("2")) {
                    String name = gAdapter.getItem(position).getUniversity();
                    Intent ai = new Intent(AtyGoPinSelect.this, AtyGoPin.class);
                    ai.putExtra("name", name);
                    setResult(RESULT_OK, ai);
                    finish();
                } else {
                    showToast("学校即将开放，尽请期待");
                }
            }
        });
        Bitmap bitmap=getIntent().getParcelableExtra("image");
        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
//        BlurringView mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
//        View blurredView = findViewById(R.id.blurred_view);
//        mBlurringView.setBlurredView(blurredView);
    }

    public void back(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ai = new Intent(AtyGoPinSelect.this, AtyGoPin.class);
            setResult(RESULT_OK, ai);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
