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
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jl.atys.chat.domain.User;
import com.jl.atys.chat.widget.Sidebar;
import com.jl.basic.AtySupport;
import com.jl.basic.Constant;
import com.jl.utils.UserTools;

import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 简单的好友Adapter实现
 */
public class ContactAdapter extends ArrayAdapter<User> implements SectionIndexer {

    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private Sidebar sidebar;
    private int res;
    private Context context;

    public ContactAdapter(Context context, int resource, List<User> objects, Sidebar sidebar) {
        super(context, resource, objects);
        this.res = resource;
        this.sidebar = sidebar;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(res, null);
        }
        convertView.setVisibility(View.VISIBLE);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
        TextView nameTextview = (TextView) convertView.findViewById(R.id.name);
        //TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
        User user = getItem(position);
        if (user == null)
            Log.d("ContactAdapter", position + "");
        //设置nick，demo里不涉及到完整user，用username代替nick显示
        assert user != null;
        String name;
        String header;
        try {
            name = user.getName();
            header = user.getHeader();

        }catch (Exception e){
            name = "";
            header = "";
        }
        if (name.equals(Constant.NEW_FRIEND_USERNAME)) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            nameTextview.setText(name);
            if (unreadMsgView != null)
                unreadMsgView.setVisibility(View.INVISIBLE);
            //设置通讯录头像。转发消息头像
            AtySupport atySupport = (AtySupport) context;
            try {
                UserTools.displayImage(user.getPortrait(), avatar, atySupport.getOptions());
            }catch (Exception e){
                UserTools.displayImage("", avatar, atySupport.getOptions());
            }
        }
        return convertView;
    }

    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        List<String> list = new ArrayList<String>();
        list.add(getContext().getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {
            String letter = getItem(i).getHeader();
            System.err.println("contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getName());
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

}
