package com.luxg.course.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.luxg.course.model.SyllabusDayInfo;

import java.util.ArrayList;
import java.util.List;

public class SyllabusDatabase {
    private final DbHelper dbHelper;

    public SyllabusDatabase(Context context) {
        super();
        dbHelper = new DbHelper(context);
    }
    /**
     * 課表每條的詳情
     *  week_day_no 周几
     *  course_name 课程名字
     *  course_address 上课地点
     *  course_time 上课时间（第一大节=1，第2大节=2，第3大节=3，第4大节=4，晚自习=5）
     *  course_comments 备注
     */

    /**
     * //	 * 添加个人课程表信息
     * //   更新个人课程表信息
     */
    public void insertCourseTimeTable(SyllabusDayInfo data) {
        if (!isExistCourseTime(data.getWeek_day_no(), data.getCourse_time())) {
            String sql = "insert into " + DbHelper.TABLE_TIME_NAME;
            sql += "(week_day_no,course_name,course_address,course_time,course_comments) values(?,?,?,?,?)";
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            sqlite.execSQL(sql, new String[]{data.getWeek_day_no() , data.getCourse_name() , data.getCourse_address() , data.getCourse_time() , data.getCourse_comments() });
            if (sqlite.isOpen()) {
                sqlite.close();
            }
        }else {
            String sql = "update " + DbHelper.TABLE_TIME_NAME ;
            sql += " set course_name=? , course_address=? ,course_comments=? where week_day_no=? and course_time=?";
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            sqlite.execSQL(sql, new String[]{ data.getCourse_name() , data.getCourse_address() , data.getCourse_comments() ,data.getWeek_day_no() , data.getCourse_time() });
            if (sqlite.isOpen()) {
                sqlite.close();
            }
        }

    }


    /**
     * //	 * 查询课程表信息
     * //
     */
    public List<SyllabusDayInfo> selectCourseTimeTable() {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        ArrayList<SyllabusDayInfo> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("select * from " + DbHelper.TABLE_TIME_NAME +" ORDER BY _id DESC", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            SyllabusDayInfo daySyllabus = new SyllabusDayInfo();
            daySyllabus.setWeek_day_no(cursor.getString(cursor.getColumnIndex("week_day_no")));
            daySyllabus.setCourse_name(cursor.getString(cursor.getColumnIndex("course_name")));
            daySyllabus.setCourse_address(cursor.getString(cursor.getColumnIndex("course_address")));
            daySyllabus.setCourse_time(cursor.getString(cursor.getColumnIndex("course_time")));
            daySyllabus.setCourse_comments(cursor.getString(cursor.getColumnIndex("course_comments")));
            list.add(daySyllabus);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return list;
    }

    /**
     * //	 * 查询课程表信息 这一天的课程
     * //
     */
    public List<SyllabusDayInfo> selectCourseTimeTableByDay(String weekday) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        ArrayList<SyllabusDayInfo> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("select * from " + DbHelper.TABLE_TIME_NAME + " where week_day_no=? ORDER BY _id DESC", new String[]{weekday});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            SyllabusDayInfo daySyllabus = new SyllabusDayInfo();
            daySyllabus.setWeek_day_no(weekday);
            daySyllabus.setCourse_name(cursor.getString(cursor.getColumnIndex("course_name")));
            daySyllabus.setCourse_address(cursor.getString(cursor.getColumnIndex("course_address")));
            daySyllabus.setCourse_time(cursor.getString(cursor.getColumnIndex("course_time")));
            daySyllabus.setCourse_comments(cursor.getString(cursor.getColumnIndex("course_comments")));
            list.add(daySyllabus);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return list;
    }


    /**
     * 判断这周的这节课是否已经存在了
     */
    public Boolean isExistCourseTime(String week,String time) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql =("select * from " + DbHelper.TABLE_TIME_NAME + " where week_day_no=? and course_time=?");
        Cursor cursor = sqlite.rawQuery(sql, new String[]{week , time});
        boolean isReaded = false;
        if (cursor.getCount() > 0) {
            isReaded = true;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return isReaded;
    }

    /**
     * 删除这周的这节课
     */
    public void deleteCourseTimeTable(String week,String time) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DbHelper.TABLE_TIME_NAME + " where week_day_no=? and course_time=?");
        sqlite.execSQL(sql, new String[]{week , time});
        if (sqlite.isOpen()) {
            sqlite.close();
        }
    }


}
