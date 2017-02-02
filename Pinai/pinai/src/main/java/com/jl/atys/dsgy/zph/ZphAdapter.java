package com.jl.atys.dsgy.zph;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

public class ZphAdapter extends BaseAdapter {

    private Context context = null;
    private List<ZphData> data = new ArrayList<>();

    int[] colors = new int[]{R.drawable.bg_zph_item1, R.drawable.bg_zph_item2,
            R.drawable.bg_zph_item3, R.drawable.bg_zph_item4, R.drawable.bg_zph_item5,
            R.drawable.bg_zph_item6, R.drawable.bg_zph_item7, R.drawable.bg_zph_item8
    };

    public ZphAdapter(Context context) {
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
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<ZphData> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ZphData zph =data.get(position);
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_zph_cell, null);
            holder = new Holder();
            holder.setTitle((TextView) convertView.findViewById(R.id.title));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //Log.e("TTT", ((position % 8)+ ".......111111..."));
       // Log.e("EEE", position + ".......111111...");
        holder.getTitle().setBackgroundResource(colors[position % 8]);
        holder.getTitle().setText(zph.getTitle());
        holder.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid =zph.getPostId();
                context.startActivity(new Intent(context, AtyDsgyOpen.class).putExtra("id",userid));
            }
        });
        return convertView;

    }

    private class Holder {
        private TextView title;

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }
    }
}
