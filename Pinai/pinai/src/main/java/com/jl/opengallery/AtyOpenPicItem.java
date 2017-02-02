package com.jl.opengallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.jl.basic.AtySupport;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

public class AtyOpenPicItem extends AtySupport {
    private PicItemAdpter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_open_pic_item);
        Activity a = getParent();
        init();
    }

    private void init() {
        GridView mGridView = (GridView) findViewById(R.id.child_grid);
        List<String> list = getIntent().getStringArrayListExtra("data");
        adapter = new PicItemAdpter(this, list, mGridView);
        mGridView.setAdapter(adapter);
    }

    public void submitPic(View v) {
        Intent i = new Intent(AtyOpenPicItem.this, AtyOpenPic.class);
        List<String> pathAll = adapter.getPathAll();
        i.putStringArrayListExtra("pathAll", (ArrayList) pathAll);
        i.putExtra("isok", true);
        setResult(RESULT_OK, i);
        finish();
    }

    public void back(View v) {
        finish();
    }
}
