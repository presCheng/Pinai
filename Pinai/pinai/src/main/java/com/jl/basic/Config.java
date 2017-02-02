package com.jl.basic;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Config {
    // ----------------Config-------------------------------------------------------------
    public static final String APP_ID = "hrb.jl.pinai";
    public static final String CHARSET = "utf-8";
    //public static final String SERVER_MAIN="http://192.168.31.201";
    public static final String SERVER_MAIN = "http://www.pinai521.com";
    public static final String SERVER_URL = SERVER_MAIN + "/android/api";// 服务器api接口
    public static final String SERVER_SERVICE_URL = SERVER_MAIN + "/article/privacy.html";// 服务器api接口
    public static final String TOKEN = "jciy9ldJ";// 验证脚本
    public static final String ACTION_RECOVERYPASSWORD = "recovery_password";
    public static final String ACTION_SUPPORT = "support";
    public static final String ACTION_DELETE_USERPOST = "delete_userpost";
    public static final String ACTION_SYSTEM_NOTIFICATIONS = "system_notifications";
    public static final String ACTION_UPLOAD_PORTRAIT = "uploadportrait";
    public static final String KEY_MIME = "mime";
    // ----------------Aty---------------------------------------------------------------
    public static final String ACTION_LOGIN = "login";// 登录
    public static final String ACTION_SIGN_UP = "signup";// 注册
    public static final String ACTION_COMPLETE = "complete";// 个人信息修改
    public static final String ACTION_MEMBERS_INDEX = "members_index";// 获取缘来在哪用户
    public static final String ACTION_MEMBERSSHOW = "members_show";
    public static final String ACTION_ACCOUNT = "account";//用户详细资料
    public static final String ACTION_LIKE = "like";
    public static final String ACTION_RENEW = "renew";//签到
    public static final String ACTION_SENT = "sent";
    public static final String ACTION_INBOX = "inbox";
    public static final String ACTION_ACCEPT = "accept";
    public static final String ACTION_REJECT = "reject";//拒绝
    public static final String ACTION_BLOCK = "block";
    public static final String ACTION_SETPORTRAIT = "setportrait";
    public static final String ACTION_FORUM_GETCAT = "forum_getcat";
    public static final String ACTION_FORUM_GETPOST = "forum_getpost";
    public static final String ACTION_FORUM_POSTCOMMENT = "forum_postcomment";
    // ----------------Key---------------------------------------------------------------
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ACTION = "action";
    public static final String KEY_ID = "id";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PHONE_PASSWORD = "password";
    public static final String KEY_STATUS = "status";
    // ....个人信息修改
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_CONSTELLATION = "constellation";
    public static final String KEY_PORTRAIT = "portrait";
    public static final String KEY_TAG_STR = "tag_str";
    public static final String KEY_SEX = "sex";
    public static final String KEY_BORN_YEAR = "born_year";
    public static final String KEY_GRADE = "grade";
    public static final String KEY_HOBBIES = "hobbies";
    public static final String KEY_SELF_INTRO = "self_intro";
    public static final String KEY_BIO = "bio";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_SCHOOL = "school";
    // ......缘来在哪
    public static final String KEY_PERPAGE = "perpage";
    public static final String KEY_LASTID = "lastid";
    // ......
    public static final String KEY_SENDER_ID = "senderid";
    public static final String KEY_USERID = "userid";
    //.......
    public static final String KEY_RECEIVERID = "receiverid";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_DORENEW = "dorenew";//加油
    //...........
    public static final String KEY_CATID = "catid";//论坛分类号
    public static final String KEY_NUMCHARS = "numchars";
    public static final String KEY_POSTID = "postid";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_REPLAYID = "replyid";
    public static final String KEY_COMMENTID = "commentid";
    // ----------------Status-------------------------------------------------------------
    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID = 2;// 无效的
    public static final int RESULT_STATUS_ERROR = 3;// 无效的
    public static final int RESULT_STATUS_NO_OK = 4;// 无效的
    public static final String ACTION_GET_NICKNAME = "getnickname";
    /**
     * 刷新通讯录界面
     */
    public static final String RECEIVER_REFRESH_CONTACTALL = "hrb.jl.refreshNewFriend";
    public static final String RECEIVER_ADD_USER = "hrb.jl.addUser";
    //-----------------------------BroadcastReceiver--------------------------------------------------------
    public static final String RECEIVER_REFRESH_UNREAD_MSG = "hrb.jl.refreshUnread";
    //----
    public static final String RECEIVER_REFRESH_SYSTEM_NOTIFICATIONS = "hrb.jl.system.notifications";
    //========================================================================================================
    public static final int MAX_CHAR_COUNT = 50;//字数限制
    public static final int MAX_CHAR_ZAJY_COUNT = 20;//字数限制
    public static final int MAX_CHAR_NAME_COUNT = 10;//字数限制
    public static final int MIN_GRADE = 2000;//入学年开始
    public static final int MAX_GRADE = 2015;//入学年结束
    public static final int MIN_BIRTH = 1987;//出生年开始
    public static final int MAX_BIRTH = 1997;//出生年结束
    public static final int MIN_CON = 0;//星座开始
    public static final int MAX_CON = 11;//星座结束
    public static final int MIN_SALARY = 5;//薪资开始
    public static final int MAX_SALARY= 9;//薪资结束
    public static final int MIN_PROVINCE= 1;//省份开始
    public static final int MAX_PROVINCE= 35;//省份结束
    //==================================================================================
    public static final String KEY_CAT_AZS = "1";//爱诊所
    public static final String KEY_CAT_NRB = "2";//男人帮
    public static final String KEY_CAT_NRW = "3";//女人窝
    public static final String KEY_SHOW_COUNT = "10";//女人窝
    public static final String ACTION_FORUM_POSTNEW = "forum_postnew";
    public static final String KEY_TITLE = "title";
    public static final String ACTION_UPLOADIMAGE = "uploadimage";
    public static final String KEY_NULL = "";//传空
    public static final String KEY_UN_NEW_FRIEND_READ = "unnewfriendread";//传空
    public static final String KEY_UN_SYS_READ = "unsysread";//传空
    public static final int INSERT_PIC = 123;//插图
    public static final String KEY_DATA = "data";//数据
    public static final int KEY_FORUM_UNREAD_FLAG = 10086;//消息未读notification标识
    public static final String KEY_ORIGINAL_NUMCHARS = "original_numchars";
    public static final String ACTION_GET_NOTIFICATION = "get_notifications";
    public static final String ACTION_FORUM_GETREPLY = "forum_getreply";
    public static final String ACTION_GET_USERPOSTS = "get_userposts";
    public static final String ACTION_GET_PORTRAIT = "get_portrait";
    public static final String ACTION_MATCH_USER = "match_users";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FROM = "from";
    public static final String KEY_FROM_ANDROID = "1";//来自安卓
    public static final String ACTION_OPEN_UNIVERSITY = "open_university";
    public static final String KEY_UNIVERSITY = "university";
    public static final String ACTION_GET_FORUM_UNREAD = "get_forumunread";
    public static final String ACTION_GET_OPEN_ARTICLES = "get_openarticles";
    public static final int KEY_FROM_CENTER = 10086;
    public static final int KEY_FROM_CHECK_MY = 10010;
    public static final String ACTION_MATCH_USER_RECORD ="match_users_record";
    public static final String MATCH_USER_ID ="match_users_id";
    public static final String ACTION_MARKET = "market";
    public static final String ACTION_MEMBERSRANK = "members_rank";
    public static final String ACTION_POST_LIKE_JOB = "post_like_job";
    public static final String KEY_RULE1 ="rule_1" ;
    public static final String KEY_RULE2 ="rule_2" ;
    public static final String KEY_RULE3 ="rule_3" ;
    public static final String KEY_RULE4 ="rule_4" ;
    public static final String KEY_RULE5 ="rule_5" ;
    public static final String ACTION_LIKE_JOB ="like_jobs" ;
    public static final String ACTION_GET_LIKE_JOB = "get_like_job";
    public static final String KEY_REPORT_USER_ID = "report_user_id";
    public static final String KEY_SALARY ="salary";
    public static final String KEY_PROVINCE ="province_id";
    // 填写从短信SDK应用后台注册得到的APPKEY
    public static String APPKEY = "3f9695de996a";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    public static String APPSECRET = "1b5e2489cab4860ae19eefa11f9579d8";
    public static String KEY_SMILE_UTILS = "com.jl.atys.chat.utils.SmileUtils";


    /**
     * 获得token
     *
     * @param context
     * @return String
     */
    public static String getCacheToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    /**
     * 缓存token
     *
     * @param context
     * @param token
     */
    public static void cacheToken(Context context, String token) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_TOKEN, token);
        e.apply();
    }

    /**
     * 获得ID
     *
     * @param context context
     * @return String
     */
    public static String getCacheID(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_ID, null);
    }

    /**
     * 设置ID
     *
     * @param context
     * @param id
     */
    public static void setCacheID(Context context, String id) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_ID, id);
        e.apply();
    }

    /**
     * 获得帐号
     *
     * @param context context
     * @return String
     */
    public static String getCacheUserName(Context context) {

        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_USERNAME, null);
    }

    /**
     * 设置帐号
     *
     * @param context
     * @param userName
     */
    public static void setCacheUserName(Context context, String userName) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_USERNAME, userName);
        e.apply();
    }

    /**
     * 获得头像地址
     *
     * @param context context
     * @return String
     */
    public static String getCachePortrait(Context context) {

        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_PORTRAIT, null);
    }

    /**
     * 设置头像地址
     *
     * @param context
     * @param portrait
     */
    public static void setCachePortrait(Context context, String portrait) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_PORTRAIT, portrait);
        e.apply();
    }

    /**
     * 获得性别
     *
     * @param context context
     * @return String
     */
    public static String getCacheSex(Context context) {

        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_SEX, null);
    }

    /**
     * 设置性别
     *
     * @param context
     * @param sex
     */
    public static void setCacheSex(Context context, String sex) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_SEX, sex);
        e.apply();
    }

    /**
     * 获得我的来信未读消息
     *
     * @param context context
     * @return int
     */
    public static int getCacheNewFriendUnRead(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getInt(KEY_UN_NEW_FRIEND_READ, 0);
    }

    /**
     * 设置我的来信未读消息
     *
     * @param context
     * @param unread
     */
    public static void setCacheNewFriendUnRead(Context context, int unread) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putInt(KEY_UN_NEW_FRIEND_READ, unread);
        e.apply();
    }

    /**
     * 获得系统来信未读消息
     *
     * @param context context
     * @return int
     */
    public static int getCacheSysUnRead(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getInt(KEY_UN_SYS_READ, 0);
    }

    /**
     * 设置系统来信未读消息
     *
     * @param context
     * @param unread
     */
    public static void setCacheSysUnRead(Context context, int unread) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putInt(KEY_UN_SYS_READ, unread);
        e.apply();
    }

    /**
     * 获得用户昵称
     *
     * @param context context
     * @return int
     */
    public static String getCacheNickName(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString(KEY_NICKNAME, "为设置昵称");
    }

    /**
     * 设置用户昵称
     *
     * @param context
     * @param nickName
     */
    public static void setCacheNickName(Context context, String nickName) {
        Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .edit();
        e.putString(KEY_NICKNAME, nickName);
        e.apply();
    }
}
