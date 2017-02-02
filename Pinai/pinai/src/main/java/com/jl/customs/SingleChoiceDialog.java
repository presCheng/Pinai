package com.jl.customs;

import android.content.Context;

import com.jl.utils.Utils;

import java.util.List;

import hrb.jl.pinai.R;

public class SingleChoiceDialog extends AbstractChoickDialog {
    /**
     * 单选框
     * 选择男女时候用到
     */
    private SingleChoicAdapter<String> mSingleChoicAdapter;

    public SingleChoiceDialog(Context context, List<String> list, int layout) {
        super(context, list, layout);
        initData();
    }

    protected void initData() {
        mSingleChoicAdapter = new SingleChoicAdapter<>(mContext, SingleChoiceDialog.this, mList, R.drawable.selector_checkbox2);
        mListView.setAdapter(mSingleChoicAdapter);
        mListView.setOnItemClickListener(mSingleChoicAdapter);
        Utils.setListViewHeightBasedOnChildren(mListView);
    }

    public int getSelectItem() {
        return mSingleChoicAdapter.getSelectItem();
    }

    public void setSelectItem(int item) {
        mSingleChoicAdapter.setSelectItem(item);
    }

    public SingleChoicAdapter getAdapter() {
        return mSingleChoicAdapter;
    }
}
