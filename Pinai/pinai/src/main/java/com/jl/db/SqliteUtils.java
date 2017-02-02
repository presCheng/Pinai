package com.jl.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jl.atys.gopin.GoPinSchool;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述： Sqlite连接工具
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午2:13:00
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SqliteUtils {
    private static SqliteUtils slu;

    private SqliteUtils() {
    }

    public static SqliteUtils getInstance() {
        if (slu == null) {
            slu = new SqliteUtils();
        }
        return slu;
    }

    /**
     * 连接sqlite
     *
     * @param context
     * @return SQLiteDatabase
     * @since 1.0.0
     */
    private SQLiteDatabase getConn(Context context) {
        SQLiteDatabase sql;
        DatabaseContext dbContext = new DatabaseContext(context);
        SdCardDBHelper dbHelper = SdCardDBHelper.getInstance(dbContext);
        sql = dbHelper.getWritableDatabase();
        return sql;
    }

    /**
     * 获取学校数据(目前只有黑龙江地区)
     * getSchool
     *
     * @param context
     * @param name
     * @return List<GoPinSchool>
     * @since 1.0.0
     */
    public List<GoPinSchool> getSchool(Context context, String name) {
        SQLiteDatabase sql = getConn(context);
        String query = " SELECT * FROM school WHERE name LIKE '%" + name + "%'";
        Cursor cursor = sql.rawQuery(query, null);
        List<GoPinSchool> list = new ArrayList<GoPinSchool>();
        while (cursor.moveToNext()) {
            GoPinSchool gs = new GoPinSchool();
            gs.setId(cursor.getString(cursor.getColumnIndex("id")));
            gs.setName(cursor.getString(cursor.getColumnIndex("name")));
            gs.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            gs.setCity(cursor.getString(cursor.getColumnIndex("city")));
            list.add(gs);
        }
        cursor.close();
        sql.close();
        return list;
    }

    /**
     * 获取标签
     * getSchool
     *
     * @param context
     * @return List<String>
     * List<GoPinSchool>
     * @String[] tags
     * @since 1.0.0
     */
    public List<String> getTag(Context context, String[] tags) {
        SQLiteDatabase sql = getConn(context);
        StringBuffer conditions = new StringBuffer();//查询条件
        for (int i = 0; i < tags.length; i++) {
            conditions.append("?,");
        }
        conditions.deleteCharAt(conditions.length() - 1);
        String query = "SELECT * FROM tag where id in(" + conditions.toString() + ")";
        System.out.println(query + "||||||||||||||||||||||||");
        Cursor cursor = sql.rawQuery(query, tags);
        List<String> list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        sql.close();
        return list;
    }

    public List<String> getTag(Context context) {
        SQLiteDatabase sql = getConn(context);
        String query = " SELECT * FROM tag";
        Cursor cursor = sql.rawQuery(query, null);
        List<String> list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        cursor.close();
        sql.close();
        return list;
    }
}
