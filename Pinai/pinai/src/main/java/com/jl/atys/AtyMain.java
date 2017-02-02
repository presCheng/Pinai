package com.jl.atys;

import android.app.LocalActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TabHost;
import android.widget.TextView;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMNotifier;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.jl.atys.chat.AtyChat;
import com.jl.atys.chat.domain.InviteMessage;
import com.jl.atys.chat.domain.InviteMessage.InviteMesageStatus;
import com.jl.atys.chat.domain.User;
import com.jl.atys.dsgy.AtyDsgy;
import com.jl.atys.dsgy.AtyDsgyUnRead;
import com.jl.atys.gopin.AtyGoPin;
import com.jl.atys.individualcenter.AtyIndCenter;
import com.jl.atys.sms.AtyChatAll;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.basic.Constant;
import com.jl.basic.PinApplication;
import com.jl.dao.InviteMessgeDao;
import com.jl.dao.SystemNotificationsDao;
import com.jl.dao.UserDao;
import com.jl.domain.AcceptBean;
import com.jl.domain.AcceptNotificationBean;
import com.jl.domain.FriendNotificationBean;
import com.jl.domain.SystemNotificationBean;
import com.jl.net.SystemNotifications;
import com.umeng.update.UmengUpdateAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hrb.jl.pinai.R;

/**
 * 类描述：主页面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyMain extends AtySupport implements TabHost.OnTabChangeListener {
    //private TextView title;// 标题
    private AtyGoPin atyGoPin;
    private AtyDsgy atyDsgy;
    /**
     * 透传消息BroadcastReceiver
     */
    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            EMMessage message = intent.getParcelableExtra("message");
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            int aciton = Integer.parseInt(cmdMsgBody.action);
            switch (aciton) {
                case 1:
                    try {
                        //第一次有人追你
                        String id = message.getStringAttribute("id");
                        String from = message.getStringAttribute("from");
                        String content = message.getStringAttribute("content");
                        String portrait = message.getStringAttribute("portrait");
                        String nickname = message.getStringAttribute("nickname");
                        String answer = message.getStringAttribute("answer");
                        FriendNotificationBean notificationBean = new FriendNotificationBean();
                        notificationBean.setId(id);
                        notificationBean.setFrom(from);
                        notificationBean.setContent(content);
                        notificationBean.setPortrait(portrait);
                        notificationBean.setNickname(nickname);
                        notificationBean.setAnswer(answer);
                        agreeFriends(notificationBean);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        //有人第二次追你
                        //消息的id
                        String id = message.getStringAttribute("id");
                        //对方的id
                        String from = message.getStringAttribute("from");
                        String content = message.getStringAttribute("content");
                        String nickname = message.getStringAttribute("nickname");
                        String portrait = message.getStringAttribute("portrait");
                        String answer = message.getStringAttribute("answer");
                        FriendNotificationBean notificationBean = new FriendNotificationBean();
                        notificationBean.setId(id);
                        notificationBean.setFrom(from);
                        notificationBean.setContent(content);
                        notificationBean.setPortrait(portrait);
                        notificationBean.setNickname(nickname);
                        notificationBean.setAnswer(answer);
                        agreeFriends(notificationBean);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        //接受好友邀请呗（主动被动方都是这个接口）
                        String portrait = message.getStringAttribute("portrait");
                        String nickname = message.getStringAttribute("nickname");
                        String from = message.getStringAttribute("from");
                        //String id = message.getStringAttribute("id");
                        AcceptBean ab = new AcceptBean(from, portrait, nickname);
                        addUser(ab);
                        showToast("获得一个新的好友哦");
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    //对方拒绝你
                    break;
                case 5:
                    //拉黑
                    break;
                case 6:
                    //有人评论你的帖子
                    //String id = message.getStringAttribute("id");
                    //showToast("有人评论你的帖子");
                    try {
                        int unReadNum = message.getIntAttribute("unread");
                        if (atyDsgy != null)
                            atyDsgy.unRead.setText(getTwoColorText(unReadNum + "", "条未读消息"));

                    } catch (Exception ignored) {

                    }
                    String content = "有人评论你的帖子";
                    setNotify(content);
                    break;
                case 7:
                    //有人回复了你的评论
                    try {
                        int unReadNum = message.getIntAttribute("unread");
                        if (atyDsgy != null)
                            atyDsgy.unRead.setText(getTwoColorText(unReadNum + "", "条未读消息"));
                    } catch (Exception ignored) {

                    }
                    String contenst = "有人回复了你的评论";
                    setNotify(contenst);
                    break;
                case 8:
                    //系统消息
                    try {
                        SystemNotificationBean systemNotification = new SystemNotificationBean();
                        systemNotification.setContent(message.getStringAttribute("content"));
                        systemNotification.setCreatedAt(message.getStringAttribute("created_at"));
                        systemNotification.setId(message.getIntAttribute("id") + "");
                        SystemNotificationsDao dao = new SystemNotificationsDao(context);
                        dao.saveSysNotification(systemNotification);
                        sendBroToSMSAndSMS(1);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    break;
                case 10:
                    break;
            }
        }
    };
    private AtyIndCenter atyCenter;
    // ================================================
    private NewMessageBroadcastReceiver msgReceiver;
    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private TextView unreadUserLabel;// 好友未读消息
    private TextView unreadSysLabel;// 系统未读消息
    // 账号在别处登录
    private boolean isConflict = false;
    // 刷新广播
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AcceptBean acceptBean = (AcceptBean) intent.getSerializableExtra("acceptBean");
            addUser(acceptBean);
        }
    };
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
                int count = (Integer) intent.getExtras().get("count");
                setUnreadSysLabel(count);
            }
        }
    };
    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);

                if (msg != null) {

                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (AtyChat.activityInstance != null) {
                        if (msg.getChatType() == ChatType.Chat) {
                            if (from.equals(AtyChat.activityInstance
                                    .getToChatUsername()))
                                return;
                        }
                    }

                    msg.isAcked = true;
                }
            }

        }
    };
    private long exitTime = 0;
    private android.app.AlertDialog.Builder conflictBuilder;
    private boolean isConflictDialogShow;

    /**
     * 获取未读好友消息数
     *
     * @return int
     */
    public static int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }
    //private RelativeLayout topBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        //title = (TextView) findViewById(R.id.title);
       // topBar = (RelativeLayout) findViewById(R.id.top_bar);
       // title.setText(getString(R.string.aty_index));
        //变色龙
        setSystemBar(1);
        init(savedInstanceState);
        initChat();
        //-------友盟更新-----------------------
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(this);
        //-------获取服务器推送的系统消息
        new SystemNotifications(Config.getCacheID(context), new SystemNotifications.SuccessCallback() {
            @Override
            public void onSuccess(List<SystemNotificationBean> systemNotificationBeans, List<FriendNotificationBean> friendNotificationBeans, List<AcceptNotificationBean> acceptNotificationBeans) {
                SystemNotificationsDao dao = new SystemNotificationsDao(context);
                //这里进行插入判断，如果插入成功才更新小红点
                if (dao.saveSysNotifactions(systemNotificationBeans)) {
                    if (systemNotificationBeans.size() != 0) {
                        //如果未读系统消息大于0,发送小红点
                        Config.setCacheSysUnRead(context, Config.getCacheSysUnRead(context) + systemNotificationBeans.size());
                    }
                }
                setUnreadSysLabel(Config.getCacheSysUnRead(context));
                //------------------------------------------------------------------
                for (int i = 0; i < friendNotificationBeans.size(); i++) {
                    FriendNotificationBean notification = friendNotificationBeans.get(i);
                    agreeFriends(notification);
                }
                for (int i = 0; i < acceptNotificationBeans.size(); i++) {
                    AcceptNotificationBean notification = acceptNotificationBeans.get(i);
                    AcceptBean ab = new AcceptBean(notification.getFrom(), notification.getPortrait(), notification.getNickname());
                    addUser(ab);
                }
            }

        }, new SystemNotifications.FailCallback() {
            @Override
            public void onFail(String error) {

            }
        });
        filterSystemNotifications();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    private void initChat() {
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        // 接受到消息回调函数
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
                .getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(
                new UserContactListener());
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(
                new UserConnectionListener());
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
        // 注册一个cmd消息的BroadcastReceiver
        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        registerReceiver(cmdMessageReceiver, cmdIntentFilter);
        /**
         * 监听添加好友处理广播事件
         */
        IntentFilter addUserintentFilter = new IntentFilter();
        addUserintentFilter.addAction(Config.RECEIVER_ADD_USER);
        registerReceiver(mRefreshBroadcastReceiver, addUserintentFilter);
    }

    /**
     * 监听系统的发来的消息
     */
    private void filterSystemNotifications() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS);
        registerReceiver(systemNotificationsroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收者
        try {
            unregisterReceiver(msgReceiver);
        } catch (Exception ignored) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
        } catch (Exception ignored) {
        }
        try {
            unregisterReceiver(mRefreshBroadcastReceiver);
        } catch (Exception ignored) {
        }
        try {
            unregisterReceiver(cmdMessageReceiver);
        } catch (Exception ignored) {
        }
        try {
            unregisterReceiver(systemNotificationsroadcastReceiver);
        } catch (Exception ignored) {
        }
        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
    }

    /**
     * 设置通知栏消息
     * 跳转到帖子未读消息界面
     *
     * @param content 通知栏显示内容
     */
    private void setNotify(String content) {
        String ns = NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.logo;
        CharSequence tickerText = "有消息来了";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify_message);
        contentView.setTextViewText(R.id.notify_title, tickerText);
        contentView.setTextViewText(R.id.notify_content, content);
        //contentView.setImageViewResource(R.id.notify_img,R.drawable.logo);
        notification.contentView = contentView;
        Intent intent = new Intent(this, AtyDsgyUnRead.class);
        notification.contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //通知时发出默认的声音
        notification.defaults = Notification.DEFAULT_SOUND;
        mNotificationManager.notify(Config.KEY_FORUM_UNREAD_FLAG, notification);
    }

    /**
     * 设置通知栏消息
     *
     * @param content 通知栏显示内容
     * @param id      点击跳转的用户信息id
     * @param answer  答案
     */
    private void setNotify(String content, String id, String answer) {
        String ns = NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        //定义通知栏展现的内容信息
        int icon = R.drawable.logo;
        CharSequence tickerText = "有消息来了";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify_message);
        contentView.setTextViewText(R.id.notify_title, tickerText);
        contentView.setTextViewText(R.id.notify_content, content);
        //contentView.setImageViewResource(R.id.notify_img,R.drawable.logo);
        notification.contentView = contentView;
        Intent intent = new Intent(this, AtyNotify.class);
        //传对方Id
        intent.putExtra("id", id);
        intent.putExtra("answer", answer);
        notification.contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //点击后自动清除，图示项只有这样设置后才能在点击后自动删除
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //通知时发出默认的声音
        notification.defaults = Notification.DEFAULT_SOUND;
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知 id不同才能显示不同的通知
        mNotificationManager.notify(Integer.parseInt(id), notification);
    }

    private SpannableStringBuilder getTwoColorText(String str1, String str2) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1 + " " + str2);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background_pink)), 0, str1.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 刷新未读消息数
     * 消息的小圆点
     * 代表是未读好友消息
     */
    public void updateUnreadUserLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadUserLabel.setText(String.valueOf(count));
            unreadUserLabel.setVisibility(View.VISIBLE);
            sendBroToSMSAndChatAll(count);
        } else {
            unreadUserLabel.setVisibility(View.INVISIBLE);
            sendBroToSMSAndChatAll(0);
        }
    }

    /**
     * 发送0 表示未读
     * sendBroToSMSAndChatAll
     * 来好友消息
     *
     * @param count void
     * @since 1.0.0
     */
    private void sendBroToSMSAndChatAll(int count) {
        Intent intent = new Intent();
        intent.putExtra("count", count);
        intent.setAction(Config.RECEIVER_REFRESH_UNREAD_MSG);
        sendBroadcast(intent);
    }

    /**
     * 发送0 表示未读
     * sendBroToSMSAndSMS
     * 系统消息
     *
     * @param count void
     * @since 1.0.0
     */
    private void sendBroToSMSAndSMS(int count) {
        Config.setCacheSysUnRead(context, Config.getCacheSysUnRead(context) + count);
        int unReadCount = Config.getCacheSysUnRead(context);
        Intent intent = new Intent();
        //intent.setPackage("AtyMain");
        intent.putExtra("count", unReadCount);
        intent.setAction(Config.RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS);
        sendBroadcast(intent);
        //这里做一个消息显示小红点
        setUnreadSysLabel(unReadCount);
        //这里接受系统返回的透传消息
    }

    /**
     * 设置系统消息的小红点显示
     * 通过getCacheSysUnRead获得未读消息
     */
    private void setUnreadSysLabel(int unReadCount) {
        if (unReadCount > 0) {
            unreadSysLabel.setVisibility(View.VISIBLE);
            unreadSysLabel.setText(Config.getCacheSysUnRead(context) + "");
        } else {
            unreadSysLabel.setVisibility(View.GONE);
        }
    }

    /**
     * 接收到添加好友邀请(同意或拒绝)，
     *
     * @param notification 消息对象
     */
    private void agreeFriends(FriendNotificationBean notification) {
        //把id转换成int
        int ids = Integer.parseInt(notification.getId());
        String from = notification.getFrom();
        String nickname = notification.getNickname();
        String reason = notification.getContent();
        String portrait = notification.getPortrait();
        String answer = notification.getAnswer();
        List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
        for (InviteMessage inviteMessage : msgs) {
            if (inviteMessage.getId() == ids) {
                //这里进行好友是否被添加到本地,如果有就不添加
                return;
                //inviteMessgeDao.deleteMessage(id);
            }
        }
        // 自己封装的javabean
        InviteMessage msg = new InviteMessage();
        msg.setId(ids);
        msg.setFrom(from);
        msg.setNickname(nickname);
        msg.setTime(System.currentTimeMillis());
        //this is
        msg.setReason(answer);
        msg.setPortrait(portrait);
        // 设置相应status
        msg.setStatus(InviteMesageStatus.BEAPPLYED);

        //保存信息到数据库
        saveInviteMsg(msg);
        notifyNewIviteMessage();
        Log.d("Y", "agreeFriends" + reason);
        setNotify(reason, from, answer);

    }

    /**
     * 被添加和主动添加都调用该方法写入数据库
     *
     * @param ab
     */
    private void addUser(AcceptBean ab) {
        if (ab != null) {
            // 保存增加的联系人
            Map<String, User> localUsers = PinApplication.getInstance()
                    .getContactList();
            Map<String, User> toAddUsers = new HashMap<>();
            String id = ab.getId();
            String nickname = ab.getNickname();
            User user = setUserHead(nickname);
            //这个username就是环信的id
            user.setUsername(ab.getId());
            user.setPortrait(ab.getPortrait());
            user.setName(nickname);
            //如果该用户不存在
            if (!localUsers.containsKey(id)) {
                userDao.saveContact(user);
                toAddUsers.put(id, user);
                //加入到数据库中
                localUsers.putAll(toAddUsers);
            }
            // 刷新AtyContactAll，如果添加好友了，
            // 就刷新通讯录界面，如果没有添加，
            // 那么也要刷新，因为要做未读和已读小红点处理
            sendBroToContactAll();
            // 提示有新消息 手机震动和声音提示
            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
        }
        Log.i("Y", "addUser");
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

    /**
     * 提示手机
     */
    private void notifyNewIviteMessage() {

        // 提示有新消息 手机震动和声音提示
        EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        //未读加一
        Config.setCacheNewFriendUnRead(context, Config.getCacheNewFriendUnRead(context) + 1);
        //刷新通讯录界面
        sendBroToContactAll();

    }

    /**
     * set head
     *
     * @param userName 用户名
     * @return User
     */
    User setUserHead(String userName) {
        User user = new User();
        user.setUsername(userName);
        String headerName;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (userName.equals(Constant.NEW_FRIEND_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance()
                    .get(headerName.substring(0, 1)).get(0).target.substring(0,
                            1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow)
            showConflictDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isConflict) {
            updateUnreadUserLabel();
            EMChatManager.getInstance().activityResumed();
        }

        Log.e("E", "Main resume......");
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        Log.e("tag", "onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次返回到桌面");
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        PinApplication.getInstance().logout();

        if (!AtyMain.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(
                            AtyMain.this);
                conflictBuilder.setTitle("下线通知");
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                finish();
                                startActivity(new Intent(AtyMain.this,
                                        AtyLogin.class));
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                Log.e("###",
                        "---------color conflictBuilder error" + e.getMessage());
            }

        }
    }

    public void putChildGoPin(AtyGoPin atyGoPin) {
        this.atyGoPin = atyGoPin;
    }

    public void putChildDsgy(AtyDsgy atyDsgy) {
        this.atyDsgy = atyDsgy;
    }

    public void putChildIndCenter(AtyIndCenter atyGoPin) {
        this.atyCenter = atyGoPin;
    }

    /**
     * 初始化 void
     */
    @SuppressWarnings("deprecation")
    private void init(Bundle savedInstanceState) {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // 神奇的代码， 控制传入 setContent（）； 里面传入 intent 异常
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(
                this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager); // 设置TabHost
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.aty_index))
                .setIndicator(setStyle(1))
                .setContent(new Intent(this, AtyDsgy.class)));// R.id.main_index
        tabHost.addTab(tabHost
                .newTabSpec(getString(R.string.aty_danshengongyu))
                .setIndicator(setStyle(2))
                .setContent(new Intent(this, AtyGoPin.class)));
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.aty_xinxi))
                .setIndicator(setStyle(3))
                .setContent(new Intent(this, AtyChatAll.class)));
        tabHost.addTab(tabHost
                .newTabSpec(getString(R.string.aty_gerenzhongxin))
                .setIndicator(setStyle(4))
                .setContent(new Intent(this, AtyIndCenter.class)));
        tabHost.setOnTabChangedListener(this);
    }

    public RelativeLayout setStyle(int tag) {
        RelativeLayout relativeLayout = (RelativeLayout) RelativeLayout
                .inflate(this, R.layout.tab_style, null);
        ImageView view = (ImageView) relativeLayout.findViewById(R.id.iv_tbh);
        TextView name = (TextView) relativeLayout.findViewById(R.id.tv_tb);
        switch (tag) {
            case 1:
                name.setText(getString(R.string.aty_index));
                view.setBackgroundResource(R.drawable.tbh_mindex);
                break;
            case 2:
                name.setText(getString(R.string.aty_danshengongyu));
                view.setBackgroundResource(R.drawable.tbh_mdanshengongyu);
                break;
            case 3:
                name.setText(getString(R.string.aty_xinxi));
                view.setBackgroundResource(R.drawable.tbh_msms);
                //好友未读消息
                unreadUserLabel = (TextView) relativeLayout
                        .findViewById(R.id.tv_unread_user_Label);
                //系统未读消息
                unreadSysLabel = (TextView) relativeLayout
                        .findViewById(R.id.tv_unread_sys_Label);
                break;
            case 4:
                name.setText(getString(R.string.aty_gerenzhongxin));
                view.setBackgroundResource(R.drawable.tbh_mindc);
                break;
        }
        return relativeLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 传给子容器
        if (atyGoPin != null) {
            atyGoPin.onTabActivityResult(requestCode, resultCode, data);
        }
        if (atyCenter != null) {
            atyCenter.onTabActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(getString(R.string.aty_index))) {
            //topBar.setVisibility(View.VISIBLE);
            //title.setText(getString(R.string.aty_index));
            setSystemBar(1);
        } else if (tabId.equals(getString(R.string.aty_danshengongyu))) {
            //title.setText(getString(R.string.aty_danshengongyu));
            //topBar.setVisibility(View.VISIBLE);
            setSystemBar(1);
        } else if (tabId.equals(getString(R.string.aty_xinxi))) {
            //title.setText(getString(R.string.aty_xinxi));
            //topBar.setVisibility(View.VISIBLE);
            setSystemBar(1);
        } else if (tabId.equals(getString(R.string.aty_gerenzhongxin))) {
            //topBar.setVisibility(View.GONE);
            //title.setText("");
            setSystemBar(0);
        }
    }

    /**
     * 新消息广播接收者
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
            String from = intent.getStringExtra("from");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
            if (AtyChat.activityInstance != null) {
                if (message.getChatType() == ChatType.GroupChat) {
                    if (message.getTo().equals(
                            AtyChat.activityInstance.getToChatUsername()))
                        return;
                } else {
                    if (from.equals(AtyChat.activityInstance
                            .getToChatUsername()))
                        return;
                }
            }
            // 注销广播接收者，否则在AtyChat中会收到这个广播
            abortBroadcast();
            // 刷新bottom bar消息未读数
            updateUnreadUserLabel();

        }
    }

    /**
     * 好友状态变化listener
     */
    private class UserContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            Log.e("Y", "onContactAdded");
        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = PinApplication.getInstance()
                    .getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    if (AtyChat.activityInstance != null
                            && usernameList
                            .contains(AtyChat.activityInstance
                                    .getToChatUsername())) {
                        //showToast(ChatActivity.activityInstance.getToChatUsername() + "已把你从他好友列表里移除");
                        AtyChat.activityInstance.finish();
                    }
                    updateUnreadUserLabel();
                }
            });
            Log.e("Y", "onContactDeleted");
        }

        @Override
        public void onContactInvited(String username, String reason) {
            //自己的方法实现改接口
            //agreeFriends(username, reason);
            Log.e("Y", "onContactInvited" + username);
        }

        @Override
        public void onContactAgreed(String username) {
            Log.e("Y", "onContactAgreed" + username);
        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.e("Y", "onContactRefused" + username);
        }
    }

    /**
     * 用户连接状态监听listener
     */
    private class UserConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // chatHistoryFragment.errorItem.setVisibility(View.GONE);
                    showToast("连接成功");
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showToast("您的账号已在其他设备登录，请检查账号安全");
                        showConflictDialog();
                    } else {
                        if (!NetUtils.hasNetwork(AtyMain.this))
                            showToast("亲，请检查您的网络连接哟");
                    }
                }

            });
        }
    }
}
