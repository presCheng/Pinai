/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jl.atys.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.jl.atys.AtyNotify;
import com.jl.atys.chat.domain.InviteMessage;
import com.jl.atys.chat.domain.InviteMessage.InviteMesageStatus;
import com.jl.basic.AtySupport;
import com.jl.utils.UserTools;

import java.util.Date;
import java.util.List;

import hrb.jl.pinai.R;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {
    private Context context;

    public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.row_invite_msg, null);
            holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
            holder.reason = (TextView) convertView.findViewById(R.id.message);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (Button) convertView.findViewById(R.id.user_state);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final InviteMessage msg = getItem(position);
        if (msg != null) {
            AtySupport atySupport = (AtySupport) context;
            UserTools.displayImage(msg.getPortrait(), holder.avator, atySupport.getOptions());
            holder.reason.setText(msg.getReason());
            holder.name.setText(msg.getNickname());//这个是用户昵称，先用组名代替下
            holder.time.setText(DateUtils.getTimestampString(new
                    Date(msg.getTime())));
            if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
                //没做处理的时候
                holder.reason.setText(msg.getReason());
                holder.status.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, AtyNotify.class).putExtra("id", msg.getFrom()));
                    }
                });
            } else if (msg.getStatus() == InviteMesageStatus.AGREED) {
                holder.status.setText("已同意");
                holder.status.setEnabled(false);
            } else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
                holder.status.setText("已拒绝");
                holder.status.setEnabled(false);
            }
            // 设置用户头像
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView avator;
        TextView name;
        TextView time;
        TextView reason;
        Button status;
    }

}
