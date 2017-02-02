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
package com.jl.atys.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jl.atys.chat.adapter.NewFriendsMsgAdapter;
import com.jl.atys.chat.domain.InviteMessage;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.ConfirmAlertDialog;
import com.jl.dao.InviteMessgeDao;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 好友申请
 */
public class AtyNewFriendsMsg extends AtySupport {
    private NewFriendsMsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉未读消息标识
        Config.setCacheNewFriendUnRead(context, 0);
        setContentView(R.layout.aty_new_friends_msg);
        ListView listView = (ListView) findViewById(R.id.list);
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        //设置adapter
        adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        newFriendsIsNull();
    }

    /**
     * 是否有记录
     */
    private void newFriendsIsNull(){
        if (adapter.getCount()==0){
            findViewById(R.id.null_sms).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.null_sms).setVisibility(View.GONE);
        }
    }


    public void back(View view) {
        finish();
    }

    public void delete(View v) {
        deleteTheMsg();

    }


    /**
     * 删除消息
     */
    private void deleteTheMsg() {
        //退出
        ConfirmAlertDialog.Builder builder = new ConfirmAlertDialog.Builder(
                context);
        builder.setTitle("提示").setContent("确认删除吗？").setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        InviteMessgeDao dao = new InviteMessgeDao(context);
                        dao.deleteAllMessage();
                        refresh();
                        dialog.cancel();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    // 刷新ui
    public void refresh() {
        try {
            // 可能会在子线程中调到这方法
            runOnUiThread(new Runnable() {
                public void run() {
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    newFriendsIsNull();
                    Config.setCacheSysUnRead(context, 0);
                    //刷新通讯录界面
                    sendBroToContactAll();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新通讯录界面
     * sendBroToContactAll
     *
     * @since 1.0.0
     */
    private void sendBroToContactAll() {
        Intent intent = new Intent();
        intent.setAction(Config.RECEIVER_REFRESH_CONTACTALL);
        sendBroadcast(intent);
    }
}
