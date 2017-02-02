package com.jl.atys.dsgy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * User: GunnarXu
 * Desc: 更多的面板GridView适配器
 * Date: 2015-01-09
 * Time: 16:01
 * FIXME
 */
public class MoreAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private Context context;

    public MoreAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        mDatas = new ArrayList<String>();
        mDatas.add("表情");
        mDatas.add("图片");
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_more_item, parent, false);
        }
        String titleText = mDatas.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.more_item_image);
        TextView title = (TextView) convertView.findViewById(R.id.more_item_title);
        if (position == 0) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_icon_selector));
            title.setText(titleText);
        } else if (position == 1) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_image_selector));
            title.setText(titleText);
        }
        return convertView;
    }
}
