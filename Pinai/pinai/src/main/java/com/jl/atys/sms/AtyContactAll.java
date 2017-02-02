package com.jl.atys.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.easemob.chat.EMContactManager;
import com.jl.atys.chat.AtyChat;
import com.jl.atys.chat.adapter.ContactAdapter;
import com.jl.atys.chat.domain.User;
import com.jl.atys.chat.widget.Sidebar;
import com.jl.atys.gopin.AtyGoPinProfile;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.basic.Constant;
import com.jl.basic.PinApplication;
import com.jl.dao.InviteMessgeDao;
import com.jl.dao.UserDao;
import com.jl.net.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hrb.jl.pinai.R;

/**
 * 联系人列表列
 * 类名称：AtyContactAll
 * 类描述：
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-29 下午1:33:29
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyContactAll extends AtySupport {
    private ContactAdapter adapter;
    private List<User> contactList;
    private List<String> blackList;
    // 刷新广播
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_contact_all);
        ListView listView = (ListView) findViewById(R.id.list);
        Sidebar sidebar = (Sidebar) findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        //黑名单列表
        blackList = EMContactManager.getInstance().getBlackListUsernames();
        contactList = new ArrayList<>();
        // 获取设置contactlist
        getContactList();
        // 设置adapter
        adapter = new ContactAdapter(AtyContactAll.this, R.layout.row_contact, contactList, sidebar);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = adapter.getItem(position).getUsername();
                if (Constant.NEW_FRIEND_USERNAME.equals(username)) {
//                    // 进入申请与通知页面
//                    User user = PinApplication.getInstance().getContactList().get(Constant.NEW_FRIEND_USERNAME);
//                    user.setUnreadMsgCount(0);
//                    startActivity(new Intent(AtyContactAll.this, AtyNewFriendsMsg.class));
                } else {
                    //demo中直接进入聊天页面，实际一般是进入用户详情页
                    Intent i = new Intent(AtyContactAll.this, AtyChat.class);
                    i.putExtra("portrait", adapter.getItem(position).getPortrait());
                    i.putExtra("userId", adapter.getItem(position).getUsername());
                    i.putExtra("userName", adapter.getItem(position).getName());
                    startActivity(i);
                    finish();
                }
            }
        });
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                closeInput();
                return false;
            }
        });
        registerForContextMenu(listView);
        reflesh();
        chatIsNull();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 长按前一个不弹menu
        if (((AdapterContextMenuInfo) menuInfo).position >= 1) {
            getMenuInflater().inflate(R.menu.context_contact_list, menu);
        }
    }

    /**
     * 是否有聊天记录
     */
    private void chatIsNull(){
        if (adapter.getCount()==1){
            findViewById(R.id.null_sms).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            User tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            // 删除此联系人
            deleteContact(tobeDeleteUser);
            // 删除相关的邀请消息
            InviteMessgeDao dao = new InviteMessgeDao(AtyContactAll.this);
            dao.deleteMessage(tobeDeleteUser.getUsername());
            return true;
        } else if (item.getItemId() == R.id.check_out) {
            //查看用户信息
            User user = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            String receiverId = user.getUsername();
            startActivity(new Intent(context, AtyGoPinProfile.class).putExtra("receiverId", receiverId));
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 监听广播事件
     */
    private void reflesh() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.RECEIVER_REFRESH_CONTACTALL);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mRefreshBroadcastReceiver);
        super.onDestroy();
    }

    /**
     * 删除联系人
     *
     * @param tobeDeleteUser
     */
    public void deleteContact(final User tobeDeleteUser) {
        showProgressDialog(AtyContactAll.this, "提示", "正在删除.....", true);
        new Thread(new Runnable() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //adapter.remove(tobeDeleteUser);
                            //adapter.notifyDataSetChanged();
                            new Block(Config.getCacheID(context), tobeDeleteUser.getUsername(), new Block.SuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    showToast("删除成功");
                                    //EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
                                    // 删除db和内存中此用户的数据
                                    UserDao dao = new UserDao(AtyContactAll.this);
                                    dao.deleteContact(tobeDeleteUser.getUsername());
                                    PinApplication.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                                    refresh();
                                    chatIsNull();
                                    closeProgressDialog();
                                }
                            }, new Block.FailCallback() {
                                @Override
                                public void onFail(String error) {
                                    closeProgressDialog();
                                    showToast("删除异常");
                                }
                            });

                        }
                    });

                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showToast("删除失败: " + e.getMessage());
                        }
                    });
                }

            }
        }).start();
    }

    // 刷新ui
    public void refresh() {
        showToast("添加新的好友啦，快去愉快的聊天吧");
        try {
            // 可能会在子线程中调到这方法
            runOnUiThread(new Runnable() {
                public void run() {
                    getContactList();
                    adapter.notifyDataSetChanged();
                    findViewById(R.id.null_sms).setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    private void getContactList() {
        contactList.clear();
        //获取本地好友列表
        Map<String, User> users = PinApplication.getInstance().getContactList();
        for (Entry<String, User> entry : users.entrySet()) {
            try {
                if (!entry.getKey().equals(Constant.NEW_FRIEND_USERNAME) && !blackList.contains(entry.getKey()))
                    contactList.add(entry.getValue());
            } catch (Exception e) {
                showToast("终于抓到你了，bug");
            }
        }
            // 排序
            Collections.sort(contactList, new Comparator<User>() {
                @Override
                public int compare(User lhs, User rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            // 把"申请与通知"添加到首位
            contactList.add(0, users.get(Constant.NEW_FRIEND_USERNAME));

    }
    public void back(View v){
        finish();
    }
}
