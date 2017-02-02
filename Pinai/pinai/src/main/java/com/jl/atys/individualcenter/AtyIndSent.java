package com.jl.atys.individualcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.basic.AtyListSupport;
import com.jl.basic.Config;
import com.jl.domain.SentBean;
import com.jl.net.Sent.FailCallback;
import com.jl.net.Sent.SuccessCallback;
import com.jl.utils.UserTools;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类名称：AtyIndChaseMe
 * 类描述：我追的人
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-10
 * 下午1:17:25
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyIndSent extends AtyListSupport {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_sent);
        init();
    }

    private void init() {
        new com.jl.net.Sent(Config.getCacheID(context), "", "10", new SuccessCallback() {
            @Override
            public void onSuccess(List<SentBean> data) {
                Adapter adapter = new Adapter();
                adapter.addAll(data);
                setListAdapter(adapter);
            }
        }, new FailCallback() {
            @Override
            public void onFail(String error) {
            }
        });
    }

    public void back(View v) {
        finish();
    }

    class Adapter extends BaseAdapter {
        private List<SentBean> data = new ArrayList<SentBean>();

        public Adapter() {
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

        public void addAll(List<SentBean> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clear() {
            data.clear();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.ind_chaseme_list_cell, null);
                holder = new Holder();
                holder.pic = (ImageView) convertView
                        .findViewById(R.id.pic);
                holder.sex = (ImageView) convertView
                        .findViewById(R.id.sex);
                holder.name = (TextView) convertView
                        .findViewById(R.id.name);
                holder.school = (TextView) convertView
                        .findViewById(R.id.school);
                holder.status = (Button) convertView
                        .findViewById(R.id.status);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            final SentBean cm = data.get(position);
            holder.name.setText(cm.getName());
            holder.school.setText(cm.getSchool());
            if (cm.getStatus().equals("0")) {
                holder.status.setText("等待中");
            } else if (cm.getStatus().equals("1")) {
                holder.status.setText("已同意");
            } else if (cm.getStatus().equals("2")) {
                holder.status.setText("已拒绝");
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AtyIndSent.this, AtyGoPinProfile.class).putExtra("receiverId", cm.getId()));
                }
            });

            if (cm.getSex().equals("F")) {
                holder.sex.setBackgroundResource(R.drawable.big_girl);
            } else {
                holder.sex.setBackgroundResource(R.drawable.big_boy);
            }
            UserTools.displayImage(cm.getPortrait(), holder.pic, getOptions());
            return convertView;
        }

        private class Holder {
            private TextView name = null;
            private ImageView sex = null;
            private TextView school = null;
            private Button status = null;
            private ImageView pic = null;
        }
    }
}
