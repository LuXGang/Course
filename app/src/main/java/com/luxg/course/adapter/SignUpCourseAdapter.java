package com.luxg.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luxg.course.R;
import com.luxg.course.model.CourseInfo;
import com.luxg.course.model.TeacherInfo;
import com.luxg.course.model.TeachingInfo;

import java.util.ArrayList;

/**
 * Created by LuXiaogang on 2016/2/25.
 */
public class SignUpCourseAdapter extends BaseAdapter {
    private ArrayList<TeachingInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public SignUpCourseAdapter(ArrayList<TeachingInfo> list, Context context ) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TeachingInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.signup_course_list_item, null);
            viewholder = new ViewHolder();
            viewholder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewholder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewholder.tv_introduce = (TextView) convertView
                    .findViewById(R.id.tv_introduce);
            viewholder.tv_address = (TextView) convertView
                    .findViewById(R.id.tv_address);
            viewholder.tv_teacher = (TextView) convertView
                    .findViewById(R.id.tv_teacher);
            viewholder.tv_number = (TextView) convertView
                    .findViewById(R.id.tv_number);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        final TeachingInfo infoteaching = list.get(position);
        CourseInfo info = infoteaching.getCourse();
        TeacherInfo tInfo = infoteaching.getTeacher();
        viewholder.tv_name.setText("课程名称：   "+ info.getCourseName());
        viewholder.tv_introduce.setText("课程简介：   "+ info.getCourseIntroduce());
        viewholder.tv_address.setText("上课地点：   "+ info.getCourseAddress());
        viewholder.tv_number.setText("上课人数：   "+ info.getCourseNumber());
        viewholder.tv_teacher.setText("授课教师：   "+ tInfo.getTeacherName());
        String day = "",time = "";
        switch (info.getCourseTimeDay()) {
            case "1": day = "星期一"; break;
            case "2": day = "星期二"; break;
            case "3": day = "星期三"; break;
            case "4": day = "星期四"; break;
            case "5": day = "星期五"; break;
            case "6": day = "星期六"; break;
            case "7": day = "星期天"; break;
        }
        switch (info.getCourseTime()) {
            case "1": time = "第一大节"; break;
            case "2": time = "第二大节"; break;
            case "3": time = "第三大节"; break;
            case "4": time = "第四大节"; break;
            case "5": time = "晚间自习"; break;
        }
        viewholder.tv_time.setText("上课时间：   " + day +" : "+time);



        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_introduce;
        private TextView tv_address;
        private TextView tv_teacher;
        private TextView tv_number;

    }


}
