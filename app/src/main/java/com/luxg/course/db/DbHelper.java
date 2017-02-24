package com.luxg.course.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private final String TAG = "DbHelper";

    private final static String DATABASE_NAME = "TimeTable.db";
    private final static int DATABASE_VERSION = 1;
    public final static String TABLE_TIME_NAME = "time_table";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }
    /**
     *  week_day_no 周几
     *  course_name 课程名字
     *  course_address 上课地点
     *  course_time 上课时间（第一大节=1，第2大节=2，第3大节=3，第4大节=4，晚自习=5）
     *  course_comments 备注
     */
    public static final String CREATE_TIME_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TIME_NAME
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "week_day_no TEXT,course_name TEXT,course_address TEXT,course_time TEXT,course_comments TEXT)";
    /**
     * 删除表
     *
     */
    private void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate");
        deleteTable(db);
        db.execSQL(CREATE_TIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade");
        onCreate(db);
    }
}
