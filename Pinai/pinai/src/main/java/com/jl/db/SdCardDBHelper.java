package com.jl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jl.basic.PinApplication;
import com.jl.dao.InviteMessgeDao;
import com.jl.dao.SystemNotificationsDao;
import com.jl.dao.UnReadDao;
import com.jl.dao.UserDao;

/**
 * 类描述：数据库管理和维护类
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24 下午12:27:09
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SdCardDBHelper extends SQLiteOpenHelper {
    public static final String TAG = "SdCardDBHelper";
    private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
            + UserDao.TABLE_NAME + " ("
            + UserDao.COLUMN_NAME_NICK + " TEXT, "
            + UserDao.COLUMN_NAME_PORTRAIT + " TEXT, "
            + UserDao.COLUMN_NAME_NAME + " TEXT, "
            + UserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";
    private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
            + InviteMessgeDao.TABLE_NAME + " ("
            + InviteMessgeDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_NICKNAME + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_PORTRAIT + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_TIME + " TEXT); ";
    private static final String INIVTE_UNREAD_FORUM_MSG_TABLE_CREATE = "CREATE TABLE "
            + UnReadDao.TABLE_NAME + " ("
            + UnReadDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UnReadDao.COLUMN_NAME_PORTRAIT + " TEXT, "
            + UnReadDao.COLUMN_NAME_RECEIVER_ID + " TEXT, "
            + UnReadDao.COLUMN_NAME_SENDER_ID + " TEXT, "
            + UnReadDao.COLUMN_NAME_CATEGORY_ID + " TEXT, "
            + UnReadDao.COLUMN_NAME_POST_ID + " TEXT, "
            + UnReadDao.COLUMN_NAME_COMMENT_ID + " TEXT, "
            + UnReadDao.COLUMN_NAME_REPLY_ID + " TEXT, "
            + UnReadDao.COLUMN_NAME_NICKNAME + " TEXT, "
            + UnReadDao.COLUMN_NAME_CREATED_AT + " TEXT, "
            + UnReadDao.COLUMN_NAME_ORIGINAL_CONTENT + " TEXT, "
            + UnReadDao.COLUMN_NAME_CONTENT + " TEXT, "
            + UnReadDao.COLUMN_NAME_CATEGORY + " TEXT);";
    private static final String INIVTE_SYSTEM_NOTIFICATIONS = "CREATE TABLE "
            + SystemNotificationsDao.TABLE_NAME + " ("
            + SystemNotificationsDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SystemNotificationsDao.COLUMN_NAME_CREATED_AT + " TEXT, "
            + SystemNotificationsDao.COLUMN_NAME_STATUS + " TEXT, "
            + SystemNotificationsDao.COLUMN_NAME_CONTENT + " TEXT);";
    /**
     * 数据库版本
     */
    public static int VERSION = 1;
    private static SdCardDBHelper instance;

    /**
     * 构造函数
     *
     * @param context 上下文环境
     */
    private SdCardDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    /**
     * 构造函数
     *
     * @param context 上下文环境
     */
    private SdCardDBHelper(Context context) {
        this(context, PinApplication.getInstance().getDBrName(), VERSION);
    }

    public static SdCardDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SdCardDBHelper(context);
        }
        return instance;
    }

    /**
     * 创建数据库时触发，创建离线存储所需要的数据库表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "开始创建数据库表");
        db.execSQL(USERNAME_TABLE_CREATE);
        db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
        db.execSQL(INIVTE_UNREAD_FORUM_MSG_TABLE_CREATE);
        db.execSQL(INIVTE_SYSTEM_NOTIFICATIONS);
        Log.e(TAG, "创建完成.........");

    }

    /**
     * 更新数据库时触发，
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "更新数据库");
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
