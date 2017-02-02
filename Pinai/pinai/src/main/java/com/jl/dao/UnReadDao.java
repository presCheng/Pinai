package com.jl.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jl.db.DatabaseContext;
import com.jl.db.SdCardDBHelper;
import com.jl.domain.UnReadBean;

import java.util.ArrayList;
import java.util.List;

/**
 * User: GunnarXu
 * Desc:
 * Date: 2015-01-12
 * Time: 12:44
 * FIXME
 */
public class UnReadDao {
    public static final String TABLE_NAME = "un_read_forum_msg";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NICKNAME = "nickname";
    public static final String COLUMN_NAME_CATEGORY = "category";
    public static final String COLUMN_NAME_RECEIVER_ID = "receiver_id";
    public static final String COLUMN_NAME_SENDER_ID = "sender_id";
    public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME_POST_ID = "post_id";
    public static final String COLUMN_NAME_COMMENT_ID = "comment_id";
    public static final String COLUMN_NAME_REPLY_ID = "reply_id";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_ORIGINAL_CONTENT = "original_content";
    public static final String COLUMN_NAME_PORTRAIT = "portrait";
    public static final String COLUMN_NAME_CREATED_AT = "created_at";
    private SdCardDBHelper dbHelper;

    public UnReadDao(Context context) {
        DatabaseContext dbContext = new DatabaseContext(context);
        dbHelper = SdCardDBHelper.getInstance(dbContext);
    }

    /**
     * 保存论坛未读消息列表
     *
     * @param unReadBeans 消息列表
     */
    public synchronized void saveUnReadList(List<UnReadBean> unReadBeans) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            for (UnReadBean u : unReadBeans) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, u.getId());
                values.put(COLUMN_NAME_NICKNAME, u.getNickname());
                values.put(COLUMN_NAME_CATEGORY, u.getCategory());
                values.put(COLUMN_NAME_RECEIVER_ID, u.getReceiverId());
                values.put(COLUMN_NAME_SENDER_ID, u.getSenderId());
                values.put(COLUMN_NAME_CATEGORY_ID, u.getCategoryId());
                values.put(COLUMN_NAME_POST_ID, u.getPostId());
                values.put(COLUMN_NAME_COMMENT_ID, u.getCommentId());
                values.put(COLUMN_NAME_REPLY_ID, u.getReplyId());
                values.put(COLUMN_NAME_CONTENT, u.getContent());
                values.put(COLUMN_NAME_ORIGINAL_CONTENT, u.getOriginalContent());
                values.put(COLUMN_NAME_PORTRAIT, u.getPortrait());
                values.put(COLUMN_NAME_CREATED_AT, u.getCreatedAt());
                db.insert(TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 保存论坛未读消息
     *
     * @param unReadBean 未读消息
     */
    public synchronized void saveUnRead(UnReadBean unReadBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_ID, unReadBean.getId());
            values.put(COLUMN_NAME_NICKNAME, unReadBean.getNickname());
            values.put(COLUMN_NAME_CATEGORY, unReadBean.getCategory());
            values.put(COLUMN_NAME_RECEIVER_ID, unReadBean.getReceiverId());
            values.put(COLUMN_NAME_SENDER_ID, unReadBean.getSenderId());
            values.put(COLUMN_NAME_CATEGORY_ID, unReadBean.getCategoryId());
            values.put(COLUMN_NAME_POST_ID, unReadBean.getPostId());
            values.put(COLUMN_NAME_COMMENT_ID, unReadBean.getCommentId());
            values.put(COLUMN_NAME_REPLY_ID, unReadBean.getReplyId());
            values.put(COLUMN_NAME_CONTENT, unReadBean.getContent());
            values.put(COLUMN_NAME_ORIGINAL_CONTENT, unReadBean.getOriginalContent());
            values.put(COLUMN_NAME_PORTRAIT, unReadBean.getPortrait());
            values.put(COLUMN_NAME_CREATED_AT, unReadBean.getCreatedAt());

            db.insert(TABLE_NAME, null, values);
        }
    }

    /**
     * 获取未读消息
     *
     * @return List<UnRead>
     */
    public List<UnReadBean> getUnReadList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<UnReadBean> unReadBeans = new ArrayList<UnReadBean>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by " + COLUMN_NAME_ID + " desc", null);
            while (cursor.moveToNext()) {
                UnReadBean unReadBean = new UnReadBean();
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
                String nickname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICKNAME));
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CATEGORY));
                String receiverId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_RECEIVER_ID));
                String senderId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SENDER_ID));
                String categoryId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CATEGORY_ID));
                String postId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_POST_ID));
                String commentId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENT_ID));
                String replyId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REPLY_ID));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT));
                String oContent = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ORIGINAL_CONTENT));
                String portrait = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PORTRAIT));
                String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CREATED_AT));
                unReadBean.setId(id + "");
                unReadBean.setNickname(nickname);
                unReadBean.setCategory(category);
                unReadBean.setReceiverId(receiverId);
                unReadBean.setSenderId(senderId);
                unReadBean.setCategoryId(categoryId);
                unReadBean.setPostId(postId);
                unReadBean.setCommentId(commentId);
                unReadBean.setReplyId(replyId);
                unReadBean.setContent(content);
                unReadBean.setOriginalContent(oContent);
                unReadBean.setPortrait(portrait);
                unReadBean.setCreatedAt(createdAt);
                unReadBeans.add(unReadBean);
            }
            cursor.close();
        }
        return unReadBeans;
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