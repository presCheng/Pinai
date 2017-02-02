package com.jl.atys.gopin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jl.atys.chat.domain.OpenUnver;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;


public class SchoolSelectAdapter extends BaseAdapter {
    private Context context = null;
    private List<OpenUnver> data = new ArrayList<OpenUnver>();

    public SchoolSelectAdapter(Context context) {
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
    public OpenUnver getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(data.get(position).getId());
    }

    public void addAll(List<OpenUnver> data) {
        this.data.addAll(data);
        OpenUnver o = new OpenUnver();
        o.setUniversity("全部");
        o.setId("0000");
        this.data.add(0, o);
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
        OpenUnver ou = data.get(position);
        if (position == 0) {
            lc.getTv().setText("全部");
        } else {
            if (ou.getStatus().equals("2")) {
                //开放的
                lc.getTv().setText(ou.getUniversity());
            } else {
                //未开放
                lc.getTv().setText(ou.getUniversity() + "(" + ou.getOpenAt() + ")");
                lc.getTv().setTextColor(context.getResources().getColor(R.color.gray_pressed));
                convertView.setEnabled(false);
            }
        }
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
