package com.jl.atys.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.jl.atys.chat.AtyChat;
import com.jl.atys.chat.AtyNewFriendsMsg;
import com.jl.atys.chat.adapter.ChatAllHistoryAdapter;
import com.jl.atys.chat.domain.User;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.basic.Constant;
import com.jl.basic.PinApplication;
import com.jl.dao.InviteMessgeDao;
import com.jl.dao.UserDao;
import com.jl.domain.EMConversationBean;
import com.jl.utils.UserTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hrb.jl.pinai.R;

/**
 * 聊天列表列
 * 类名称：AtyChatAll
 * 类描述：
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-29 下午1:33:46
 * 修改备注：
 * 显示所有会话记录，比较简单的实现，
 * 更好的可能是把陌生人存入本地，
 * 这样取到的聊天记录是可控的
 *
 * @version 1.0.0
 */
public class AtyChatAll extends AtySupport {


    public RelativeLayout errorItem;
    private ChatAllHistoryAdapter adapter;
    private Timer timer;
    private List<EMConversationBean> conversationList;
    // 刷新广播
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Config.RECEIVER_REFRESH_UNREAD_MSG)) {
                refresh();
            }
        }
    };
    private Handler handlerTimer = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
           if (hasInternetConnected()){
               errorItem.setVisibility(View.GONE);
           }else{
               errorItem.setVisibility(View.VISIBLE);
           }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_chat_all);
        ImageView head = (ImageView) findViewById(R.id.ind_head) ;
        UserTools.displayImage(Config.getCachePortrait(context), head, getOptions());
        initView();
        myRefresh();
    }

    /**
     * 未读消息刷新
     * void
     *
     * @since 1.0.0
     */
    private void myRefresh() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.RECEIVER_REFRESH_UNREAD_MSG);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    private void initView() {
        errorItem = (RelativeLayout) findViewById(R.id.rl_error_item);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    handlerTimer.sendEmptyMessage(0);
            }
        }, 0, 1000);
        conversationList = loadConversationsWithRecentChat();
        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new ChatAllHistoryAdapter(AtyChatAll.this, 1,
                conversationList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //新简历
                if (position==0){
                    //清空
                    User user = PinApplication.getInstance().getContactList().get(Constant.NEW_FRIEND_USERNAME);
                    user.setUnreadMsgCount(0);
                    startActivity(new Intent(AtyChatAll.this,AtyNewFriendsMsg.class));
                }
                //好友列表
                else if (position==1){
                    startActivity(new Intent(AtyChatAll.this,AtyContactAll.class));
                }else{
                EMConversationBean conversation = adapter.getItem(position);
                String uid = conversation.getUserName();
                String name = conversation.getName();
                if (uid.equals(PinApplication.getInstance().getUserName()))
                    showToast("不能和自己聊天");
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(AtyChatAll.this,
                            AtyChat.class);
                    // 单人聊天 这里的群聊没有做，如果以后做参照环信
                    String portrait = conversationList.get(position-2).getPortrait();
                    intent.putExtra("portrait", portrait);
                    intent.putExtra("userId", uid);
                    intent.putExtra("userName", name);
                    startActivity(intent);
                }
                }

            }
        });
        //----
        chatIsNull();
        // 注册上下文菜单 点击listview弹出删除选项
        registerForContextMenu(listView);
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                closeInput();
                return false;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //TODO
        if (((AdapterContextMenuInfo) menuInfo).position!=0&&((AdapterContextMenuInfo) menuInfo).position!=1) {
            getMenuInflater().inflate(R.menu.delete_message, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_message) {
            EMConversationBean tobeDeleteCons = adapter
                    .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            // 删除此会话
            EMChatManager.getInstance().deleteConversation(
                    tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
                    AtyChatAll.this);
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
            adapter.remove(tobeDeleteCons);
            chatIsNull();
            adapter.notifyDataSetChanged();
            // 更新消息未读数 Mark 这里删除消息的时候,
            // 没有对未读消息的小红点进行处理
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 是否有聊天记录
     */
    private void chatIsNull(){
        if (adapter.getCount()<=2){
            findViewById(R.id.null_sms).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.null_sms).setVisibility(View.GONE);
        }
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationsWithRecentChat());
        adapter.notifyDataSetChanged();
        chatIsNull();
    }

    /**
     * 获取所有会话
     */
    private List<EMConversationBean> loadConversationsWithRecentChat() {
        UserDao u = new UserDao(context);
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations =
                EMChatManager.getInstance().getAllConversations();
        List<EMConversationBean> list = new ArrayList<EMConversationBean>();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0) {
                EMConversationBean myBean = new EMConversationBean();
                String id = conversation.getUserName();
                myBean.setLastMessage(conversation.getLastMessage());
                try {
                    User user = u.getContact(id);
                    myBean.setName(user.getName());
                    myBean.setPortrait(user.getPortrait());
                } catch (Exception ignored) {
                }
                myBean.setGroup(conversation.isGroup());
                myBean.setUserName(id);
                myBean.setMsgCount(conversation.getMsgCount());
                myBean.setUnreadMsgCount(conversation.getUnreadMsgCount());
                list.add(myBean);
            }
        }
        // 排序
        sortConversationByLastChatTime(list);
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(
            List<EMConversationBean> conversationList) {
        Collections.sort(conversationList, new Comparator<EMConversationBean>() {
            @Override
            public int compare(final EMConversationBean con1,
                               final EMConversationBean con2) {
                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage
                        .getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage
                        .getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }
}
