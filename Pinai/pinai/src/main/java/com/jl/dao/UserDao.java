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

import com.easemob.util.HanziToPinyin;
import com.jl.atys.chat.domain.User;
import com.jl.basic.Constant;
import com.jl.db.DatabaseContext;
import com.jl.db.SdCardDBHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {
    public static final String TABLE_NAME = "uers";
    //用户id,因为环信的方法中根据username给指定用户发送消息，
    // 他封装的方法中用的就是根据username来发送的，所以这个字段只能是用来存储id
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    //用户昵称
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PORTRAIT = "portrait";//头像地址
    private SdCardDBHelper dbHelper;

    public UserDao(Context context) {
        DatabaseContext dbContext = new DatabaseContext(context);
        dbHelper = SdCardDBHelper.getInstance(dbContext);
    }

    /**
     * 保存好友list
     *
     * @param contactList
     */
    public void saveContactList(List<User> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            //db.delete(TABLE_NAME, null, null);
            for (User user : contactList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, user.getUsername());
                if (user.getNick() != null)
                    values.put(COLUMN_NAME_NICK, user.getNick());
                if (user.getPortrait() != null)
                    values.put(COLUMN_NAME_PORTRAIT, user.getPortrait());
                if (user.getName() != null)
                    values.put(COLUMN_NAME_NAME, user.getName());
                db.replace(TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 返回一个联系人信息
     *
     * @param idi 用户Id
     * @return User
     */
    public User getContact(String idi) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = new User();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */ + " where " + COLUMN_NAME_ID + " =  ?", new String[]{idi});
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String protrait = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PORTRAIT));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                user.setUsername(id);//用户id
                user.setName(name);//用户昵称
                user.setPortrait(protrait);
                user.setNick(nick);
//                String headerName = null;
//                if (!TextUtils.isEmpty(user.getNick())) {
//                    headerName = user.getNick();
//                } else {
//                    headerName = user.getName();
//                }
//                if (name.equals(Constant.NEW_FRIENDS_USERNAME) || name.equals(Constant.GROUP_USERNAME)) {
//                    user.setHeader("");
//                } else if (Character.isDigit(headerName.charAt(0))) {
//                    user.setHeader("#");
//                } else {
//                    user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
//                            .get(0).target.substring(0, 1).toUpperCase());
//                    char header = user.getHeader().toLowerCase().charAt(0);
//                    if (header < 'a' || header > 'z') {
//                        user.setHeader("#");
//                    }
//                }
//                cursor.close();
            }
        }
        return user;
    }

    /**
     * 获取好友list
     *
     * @return Map<String, User>
     */
    public Map<String, User> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, User> users = new HashMap<String, User>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String protrait = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PORTRAIT));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                User user = new User();
                user.setUsername(id);//用户id
                user.setName(name);//用户昵称
                user.setPortrait(protrait);
                user.setNick(nick);
                boolean isDigit;
                try {
                    isDigit = Character.isDigit(name.charAt(0));
                } catch (Exception e) {
                    isDigit = true;
                }
                if (name.equals(Constant.NEW_FRIEND_USERNAME) || name.equals(Constant.GROUP_USERNAME)) {
                    user.setHeader("");
                } else if (isDigit) {
                    user.setHeader("#");
                } else {
                    user.setHeader(HanziToPinyin.getInstance().get(name.substring(0, 1))
                            .get(0).target.substring(0, 1).toUpperCase());
                    char header = user.getHeader().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        user.setHeader("#");
                    }
                }
                users.put(id, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 删除一个联系人
     *
     * @param id
     */
    public void deleteContact(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_ID + "= ?", new String[]{id});
        }
    }

    /**
     * 保存一个联系人
     *
     * @param user
     */
    public void saveContact(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, user.getUsername());
        if (user.getNick() != null)
            values.put(COLUMN_NAME_NICK, user.getNick());
        if (user.getPortrait() != null)
            values.put(COLUMN_NAME_PORTRAIT, user.getPortrait());
        if (user.getName() != null)
            values.put(COLUMN_NAME_NAME, user.getName());
        if (db.isOpen()) {
            db.replace(TABLE_NAME, null, values);
        }
    }
}
