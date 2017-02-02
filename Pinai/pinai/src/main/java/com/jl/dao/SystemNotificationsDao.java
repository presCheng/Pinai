package com.jl.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jl.db.DatabaseContext;
import com.jl.db.SdCardDBHelper;
import com.jl.domain.SystemNotificationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * User: GunnarXu
 * Desc: 系统消息
 * Date: 2015-01-12
 * Time: 12:44
 * FIXME
 */
public class SystemNotificationsDao {
    public static final String TABLE_NAME = "system_notifications";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_CREATED_AT = "creat_at";
    public static final String COLUMN_NAME_STATUS = "status"; //1是未读，2是已读

    private SdCardDBHelper dbHelper;

    public SystemNotificationsDao(Context context) {
        DatabaseContext dbContext = new DatabaseContext(context);
        dbHelper = SdCardDBHelper.getInstance(dbContext);
    }

    /**
     * 保存未读消息
     *
     * @param notifications
     */
    public synchronized boolean saveSysNotifactions(List<SystemNotificationBean> notifications) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean isOk = true;
        if (db.isOpen()) {
            for (SystemNotificationBean s : notifications) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, s.getId());
                values.put(COLUMN_NAME_CONTENT, s.getContent());
                values.put(COLUMN_NAME_CREATED_AT, s.getCreatedAt());
                //这里对状态进行处理，从服务器获得的状态都是空，自己设定为1
                if (TextUtils.isEmpty(s.getStatus())) {
                    values.put(COLUMN_NAME_STATUS, "1");
                } else {
                    values.put(COLUMN_NAME_STATUS, s.getStatus());
                }
                //进行插入判断如果插入失败，那么就返回false
                if (db.insert(TABLE_NAME, null, values) == -1) {
                    isOk = false;
                }

            }
        }
        return isOk;
    }

    /**
     * 保存系统消息
     *
     * @param s
     */
    public synchronized void saveSysNotification(SystemNotificationBean s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_ID, s.getId());
            values.put(COLUMN_NAME_CONTENT, s.getContent());
            values.put(COLUMN_NAME_CREATED_AT, s.getCreatedAt());
            if (TextUtils.isEmpty(s.getStatus())) {
                values.put(COLUMN_NAME_STATUS, "1");
            } else {
                values.put(COLUMN_NAME_STATUS, s.getStatus());
            }
            db.insert(TABLE_NAME, null, values);
        }
    }

    /**
     * 获取未读系统消息
     *
     * @return List<SystemNotification>
     */
    public List<SystemNotificationBean> getSysNotifications() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<SystemNotificationBean> notifications = new ArrayList<SystemNotificationBean>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by " + COLUMN_NAME_ID + " desc", null);
            while (cursor.moveToNext()) {
                SystemNotificationBean s = new SystemNotificationBean();
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT));
                String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CREATED_AT));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STATUS));
                s.setCreatedAt(createdAt);
                s.setId(id + "");
                s.setStatus(status);
                s.setContent(content);
                notifications.add(s);
            }
            cursor.close();
        }
        return notifications;
    }

    /**
     * 更新notifiction的状态
     *
     * @param id
     * @param status
     */
    public void updateNotification(int id, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_STATUS, status);
        if (db.isOpen()) {
            db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(id)});
        }
    }


    /**
     * 删除未读消息
     *
     * @param id 消息id
     */
    public void deleteMessage(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{id});
        }
    }
}