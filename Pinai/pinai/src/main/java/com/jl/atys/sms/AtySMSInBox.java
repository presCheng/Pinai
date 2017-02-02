package com.jl.atys.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jl.basic.AtyListSupport;
import com.jl.basic.Config;
import com.jl.dao.SystemNotificationsDao;
import com.jl.domain.SystemNotificationBean;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类名称：AtySMSInBox
 * 类描述：系统消息
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-18
 * 下午2:17:25
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtySMSInBox extends AtyListSupport {
    private SystemNotificationsDao dao;
    private Adapter adapter;
    private BroadcastReceiver systemNotificationsroadcastReceiver = new BroadcastReceiver() {
        /**
         *
         * 未读系统消息广播
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS)) {
                adapter.clear();
                adapter.addAll(dao.getSysNotifications());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_sms_in_box);
        adapter = new Adapter();
        dao = new SystemNotificationsDao(context);
        adapter.addAll(dao.getSysNotifications());
        setListAdapter(adapter);
        filterSystemNotifications();
    }

    /**
     * 监听系统的发来的消息
     */
    private void filterSystemNotifications() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS);
        registerReceiver(systemNotificationsroadcastReceiver, intentFilter);
    }

    /**
     * 点击未读按钮，把未读的系统消息减一，并且推送给他的监听
     *
     * @param count
     */
    private void sendBroToSMSAndSMS(int count) {
        Config.setCacheSysUnRead(context, Config.getCacheSysUnRead(context) - count);
        int unReadCount = Config.getCacheSysUnRead(context);
        Intent intent = new Intent();
        intent.putExtra("count", unReadCount);
        //intent.setPackage("AtySMSInBox");
        intent.setAction(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(systemNotificationsroadcastReceiver);
        } catch (Exception e) {
        }
    }

    class Adapter extends BaseAdapter {
        private List<SystemNotificationBean> data = new ArrayList<SystemNotificationBean>();

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

        public void addAll(List<SystemNotificationBean> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clear() {
            data.clear();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.sms_inbox_list_cell, null);
                holder = new Holder();
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.creatAt = (TextView) convertView.findViewById(R.id.creat_at);
                holder.status = (Button) convertView.findViewById(R.id.state);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            final SystemNotificationBean s = data.get(position);
            holder.content.setText(s.getContent());
            String status = s.getStatus();
            if (status.equals("1")) {
                holder.status.setText("未读");
                holder.status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击就把状态换成已读
                        dao.updateNotification(Integer.parseInt(s.getId()), "2");
                        holder.status.setText("已读");
                        holder.status.setEnabled(false);
                        sendBroToSMSAndSMS(1);
                    }
                });
                //已读
            } else if (status.equals("2")) {
                holder.status.setText("已读");
                holder.status.setEnabled(false);
            }
            holder.creatAt.setText(s.getCreatedAt());
            return convertView;
        }

        private class Holder {
            private TextView content = null;
            private TextView creatAt = null;
            private Button status = null;
        }
    }
}
