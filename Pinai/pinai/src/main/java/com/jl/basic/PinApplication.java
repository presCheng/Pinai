package com.jl.basic;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.jl.atys.AtyMain;
import com.jl.atys.chat.AtyChat;
import com.jl.atys.chat.domain.User;
import com.jl.atys.chat.receiver.VoiceCallReceiver;
import com.jl.atys.chat.utils.PreferenceUtils;
import com.jl.dao.UserDao;
import com.jl.db.SdCardDBHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 类描述：应用主入口
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-9
 * 下午7:07:05
 * 修改备注：
 *
 * @version 1.0.0
 */
public class PinApplication extends Application {
    // login password
    private static final String PREF_PWD = "pwd";
    public static Context applicationContext;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    private static PinApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    private List<Activity> activityList = new LinkedList<Activity>();
    private String userName = null;
    private String password = null;
    private Map<String, User> contactList;

    public static PinApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
        if (processAppName == null || processAppName.equals("")) {
            // workaround for baiDu location sdk
            // 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
            // 创建新的进程。
            // 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
            // processName，
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        applicationContext = this;
        instance = this;

        // 初始化环信SDK,一定要先调用init()
        EMChat.getInstance().init(applicationContext);
        //EMChat.getInstance().setDebugMode(true);
        Log.d("EMChat Demo", "initialize EMChat SDK");
        // debugmode设为true后，就能看到sdk打印的log了
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 默认环信是不维护好友关系列表的，如果app依赖环信的好友关系，把这个属性设置为true
        options.setUseRoster(true);
        // 设置收到消息是否有新消息通知(声音和震动提示)，默认为true
        options.setNotifyBySoundAndVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
        // 设置收到消息是否有声音提示，默认为true
        options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
        // 设置收到消息是否震动 默认为true
        options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
        // 设置语音消息播放是否设置为扬声器播放 默认为true
        options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());
        // 设置notification消息点击时，跳转的intent为自定义的intent
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(applicationContext, AtyChat.class);
                ChatType chatType = message.getChatType();
                if (chatType == ChatType.Chat) { // 单聊信息
                    try {
                        String from = message.getFrom();
                        UserDao userDao = new UserDao(PinApplication.this);
                        User u = userDao.getContact(from);
                        String name = u.getName();
                        String portrait = u.getPortrait();
                        intent.putExtra("portrait", portrait);
                        intent.putExtra("userId", from);
                        intent.putExtra("userName", name);
                        intent.putExtra("chatType", AtyChat.CHATTYPE_SINGLE);
                    } catch (Exception e) {
                        Log.i("bug", "bug");
                    }
                }
                return intent;
            }
        });
        // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
        options.setNotifyText(new OnMessageNotifyListener() {
            @Override
            public String onNewMessageNotify(EMMessage message) {
                try {
                    //获取发送信息的id
                    String from = message.getFrom();
                    UserDao userDao = new UserDao(PinApplication.this);
                    User u = userDao.getContact(from);
                    String name = u.getName();
                    String oldInfo = message.getBody().toString();
                    String info = oldInfo.substring(5, oldInfo.length() - 1);
                    // 可以根据message的类型提示不同文字(可参考微信或qq)，demo简单的覆盖了原来的提示
                    return name + ":" + info;
                } catch (Exception e) {
                    return "";
                }
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                if (messageNum == 1) {
                    try {
                        String from = message.getFrom();
                        UserDao userDao = new UserDao(PinApplication.this);
                        User u = userDao.getContact(from);
                        String oldInfo = message.getBody().toString();
                        String info = oldInfo.substring(5, oldInfo.length() - 1);
                        String name = u.getName();
                        return name + ":" + info;
                    } catch (Exception e) {
                        return "";
                    }
                } else {
                    return fromUsersNum + " 个好友发来 " + messageNum + " 条消息";
                }
            }

            @Override
            public String onSetNotificationTitle(EMMessage message) {
                //修改标题
                return "聘爱";
            }

            @Override
            public int onSetSmallIcon(EMMessage message) {

                return 0;
            }

        });

        // 设置一个connectionlistener监听账户重复登陆
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());

        //注册一个语言电话的广播接收者
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingVoiceCallBroadcastAction());
        registerReceiver(new VoiceCallReceiver(), callFilter);


        initImageLoaderConfiguration();
    }

    /**
     * 初始化图片加载
     */
    private void initImageLoaderConfiguration() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "//data//data//hrb.jl.pinai//cache");
        Log.d("pic", cacheDir.getAbsolutePath());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(400, 400) //即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) //你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                        //connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    /**
     * 获取内存中好友user list
     *
     * @return Map<String, User>
     */
    public Map<String, User> getContactList() {
        if (getUserName() != null && contactList == null) {
            UserDao dao = new UserDao(applicationContext);
            //获取本地好友user list到内存,方便以后获取好友list
            contactList = dao.getContactList();
        }
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }

    /**
     * 获取当前登陆用户名
     *
     * @return String
     */
    public String getUserName() {
        if (userName == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            userName = preferences.getString(PREF_USERNAME, null);
        }
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUserName(String username) {
        if (username != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            SharedPreferences.Editor editor = preferences.edit();
            if (editor.putString(PREF_USERNAME, username).commit()) {
                userName = username;
            }
        }
    }

    /**
     * 获取数据库名称
     * 每个帐号的数据库名称都不一样
     *
     * @return String
     */
    public String getDBrName() {
        String dbName = "Pinai" + PinApplication.getInstance().getUserName() + ".db3";
        //String dbName = "Pinai_newT.db3";

        return dbName;
    }

    /**
     * 获取密码
     *
     * @return String
     */
    public String getPassword() {
        if (password == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            password = preferences.getString(PREF_PWD, null);
        }
        return password;
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_PWD, pwd).commit()) {
            password = pwd;
        }
    }

    /**
     * 退出登录,清空数据
     */
    public void logout() {
        // 先调用sdk logout，在清理app中自己的数据
        EMChatManager.getInstance().logout();
        SdCardDBHelper.getInstance(applicationContext).closeDB();
        // reset password to null
        setPassword(null);
        setContactList(null);
        Config.setCacheNewFriendUnRead(applicationContext, 0);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 添加Activity到容器中 addActivity
     *
     * @param activity void
     * @since 1.0.0
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 遍历所有Activity并finish exit void
     *
     * @since 1.0.0
     */
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }

    }

    class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onDisconnected(int error) {
            Toast.makeText(PinApplication.this, error + "onDisconnected", Toast.LENGTH_SHORT).show();
            if (error == EMError.CONNECTION_CONFLICT) {
                Intent intent = new Intent(applicationContext, AtyMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("conflict", true);
                startActivity(intent);
            }
        }

        @Override
        public void onConnected() {
            Toast.makeText(PinApplication.this, "onConnected", Toast.LENGTH_SHORT).show();
        }
    }
}
