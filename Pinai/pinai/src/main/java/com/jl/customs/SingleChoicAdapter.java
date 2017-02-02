package com.jl.customs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;


public class SingleChoicAdapter<T> extends BaseAdapter implements OnItemClickListener {

    public boolean FLAG = false;
    private Context mContext;
    private List<T> mObjects = new ArrayList<T>();
    private int mCheckBoxResourceID = 0;
    private int mSelectItem = -1;
    private OnClick oClick;
    private LayoutInflater mInflater;
    private SingleChoiceDialog dialog;

    public SingleChoicAdapter(Context context, int checkBoxResourceId) {
        init(context, checkBoxResourceId);
    }

    public SingleChoicAdapter(Context context, SingleChoiceDialog dialog, List<T> objects, int checkBoxResourceId) {
        init(context, checkBoxResourceId);
        this.dialog = dialog;
        if (objects != null) {
            mObjects = objects;
        }
    }

    public void setOnClick(OnClick onClick) {
        this.oClick = onClick;
    }

    private void init(Context context, int checkBoResourceId) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCheckBoxResourceID = checkBoResourceId;
    }

    public void refreshData(List<T> objects) {
        if (objects != null) {
            mObjects = objects;
            setSelectItem(0);
        }
    }

    public int getSelectItem() {
        return mSelectItem;
    }

    public void setSelectItem(int selectItem) {
        if (selectItem >= 0 && selectItem < mObjects.size()) {
            mSelectItem = selectItem;
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mObjects.clear();
        notifyDataSetChanged();
    }

    public int getCount() {
        return mObjects.size();
    }

    public T getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_select_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
            if (mCheckBoxResourceID != 0) {

                viewHolder.mCheckBox.setButtonDrawable(mCheckBoxResourceID);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //这个是做男女判断如果没有选择的话就没有
        viewHolder.mCheckBox.setChecked(mSelectItem == position);
        T item = getItem(position);
        if (item instanceof CharSequence) {
            viewHolder.mTextView.setText((CharSequence) item);
        } else {
            viewHolder.mTextView.setText(item.toString());
        }
        if (position == 1 && FLAG) {
            dialog.dismiss();
            FLAG = false;
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (oClick != null) {
            FLAG = true;
            mSelectItem = position;
            notifyDataSetChanged();
            oClick.onClick();
        }
    }

    public interface OnClick {
        void onClick();
    }

    public static class ViewHolder {
        public TextView mTextView;
        public CheckBox mCheckBox;
    }
}
