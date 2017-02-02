package com.jl.customs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hrb.jl.pinai.R;


public abstract class AbstractChoickDialog extends Dialog {

    protected Context mContext;
    protected View mRootView;
    protected TextView mTVTitle;
    protected ListView mListView;
    protected List<String> mList;
    private int layout;

    public AbstractChoickDialog(Context context, List<String> list, int layout) {
        super(context);
        mContext = context;
        mList = list;
        this.layout = layout;
        initView(mContext);
    }

    protected void initView(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);
        mRootView = findViewById(R.id.rootView);
        mRootView.setBackgroundDrawable(new ColorDrawable(0x0000ff00));
        mTVTitle = (TextView) findViewById(R.id.tvTitle);
        mListView = (ListView) findViewById(R.id.listView);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        ColorDrawable dw = new ColorDrawable(0x0000ff00);
        dialogWindow.setBackgroundDrawable(dw);
    }

    public void setTitle(String title) {
        mTVTitle.setText(title);
    }
}
