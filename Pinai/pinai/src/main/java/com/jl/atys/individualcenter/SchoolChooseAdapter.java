package com.jl.atys.individualcenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jl.atys.gopin.GoPinSchool;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;


public class SchoolChooseAdapter extends BaseAdapter {
    private Context context = null;
    private List<GoPinSchool> data = new ArrayList<GoPinSchool>();

    public SchoolChooseAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GoPinSchool getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(data.get(position).getId());
    }

    public void addAll(List<GoPinSchool> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_gopin_search, null);
            ListCell l = new ListCell();
            l.setTv((TextView) convertView.findViewById(R.id.gopin_search_cell));
            convertView.setTag(l);
        }
        ListCell lc = (ListCell) convertView.getTag();
        GoPinSchool gs = data.get(position);
        lc.getTv().setText(gs.getName());
        return convertView;
    }

    private static class ListCell {
        private TextView tv = null;

        public ListCell() {
        }

        public TextView getTv() {
            return tv;
        }

        public void setTv(TextView tv) {
            this.tv = tv;
        }

    }
}
