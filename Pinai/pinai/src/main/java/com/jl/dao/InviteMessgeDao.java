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
package com.jl.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jl.atys.chat.domain.InviteMessage;
import com.jl.atys.chat.domain.InviteMessage.InviteMesageStatus;
import com.jl.db.DatabaseContext;
import com.jl.db.SdCardDBHelper;

import java.util.ArrayList;
import java.util.List;

public class InviteMessgeDao {
    public static final String TABLE_NAME = "new_friends_msgs";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_FROM = "username";
    public static final String COLUMN_NAME_NICKNAME = "nickname";
    public static final String COLUMN_NAME_PORTRAIT = "portrait";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_REASON = "reason";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";

    private SdCardDBHelper dbHelper;

    public InviteMessgeDao(Context context) {
        DatabaseContext dbContext = new DatabaseContext(context);
        dbHelper = SdCardDBHelper.getInstance(dbContext);
    }

    /**
     * 保存message
     *
     * @param message
     * @return 返回这条messaged在db中的id
     */
    public synchronized Integer saveMessage(InviteMessage message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = -1;
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_ID, message.getId());
            values.put(COLUMN_NAME_FROM, message.getFrom());
            values.put(COLUMN_NAME_PORTRAIT, message.getPortrait());
            values.put(COLUMN_NAME_REASON, message.getReason());
            values.put(COLUMN_NAME_NICKNAME, message.getNickname());
            values.put(COLUMN_NAME_TIME, message.getTime());
            values.put(COLUMN_NAME_STATUS, message.getStatus().ordinal());
            db.insert(TABLE_NAME, null, values);
            Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }

            cursor.close();
        }
        return id;
    }

    /**
     * 更新message
     *
     * @param msgId
     * @param values
     */
    public void updateMessage(int msgId, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(msgId)});
        }
    }

    /**
     * 获取messges
     *
     * @return
     */
    public List<InviteMessage> getMessagesList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InviteMessage> msgs = new ArrayList<InviteMessage>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " desc", null);
            while (cursor.moveToNext()) {
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FROM));
                String portarit = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PORTRAIT));
                String nickname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICKNAME));
                String reason = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REASON));
                long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
                msg.setId(id);
                msg.setPortrait(portarit);
                msg.setNickname(nickname);
                msg.setFrom(from);
                msg.setReason(reason);
                msg.setTime(time);
                if (status == InviteMesageStatus.AGREED.ordinal())
                    msg.setStatus(InviteMesageStatus.AGREED);
                else if (status == InviteMesageStatus.REFUSED.ordinal())
                    msg.setStatus(InviteMesageStatus.REFUSED);
                else if (status == InviteMesageStatus.BEAPPLYED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.BEAPPLYED);
                }
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }

    /**
     * 返回一个联系人信息
     *
     * @param idi 用户Id
     * @return User
     */
    public InviteMessage getMessage(String idi) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        InviteMessage msg = new InviteMessage();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */ + " where " + COLUMN_NAME_FROM + " =  ?", new String[]{idi});
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FROM));
                String nickname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICKNAME));
                String portarit = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PORTRAIT));
                String reason = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REASON));
                long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
                msg.setId(id);
                msg.setPortrait(portarit);
                msg.setFrom(from);
                msg.setNickname(nickname);
                msg.setReason(reason);
                msg.setTime(time);
                if (status == InviteMesageStatus.AGREED.ordinal())
                    msg.setStatus(InviteMesageStatus.AGREED);
                else if (status == InviteMesageStatus.REFUSED.ordinal())
                    msg.setStatus(InviteMesageStatus.REFUSED);
                else if (status == InviteMesageStatus.BEAPPLYED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.BEAPPLYED);
                }
            }
        }
        return msg;
    }

    public void deleteMessage(String from) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_FROM + " = ?", new String[]{from});
        }
    }

    public void deleteAllMessage() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_FROM, null);
        }
    }
}
