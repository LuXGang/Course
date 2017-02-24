package com.luxg.course.model;

import java.io.Serializable;

/**
 * Created by LuXiaogang on 2016/1/23.
 */
public class SyllabusDayInfo implements Serializable {
        /**
         * 課表每條的詳情
         *  week_day_no 周几
         *  course_name 课程名字
         *  course_address 上课地点
         *  course_time 上课时间（第一大节=1，第2大节=2，第3大节=3，第4大节=4，晚自习=5）
         *  course_comments 备注
         */
        public String week_day_no;
        public String course_name;
        public String course_address;
        public String course_time;
        public String course_comments;

        public String getWeek_day_no() {
            return week_day_no;
        }

        public void setWeek_day_no(String week_day_no) {
            this.week_day_no = week_day_no;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getCourse_time() {
            return course_time;
        }

        public void setCourse_time(String course_time) {
            this.course_time = course_time;
        }

        public String getCourse_address() {
            return course_address;
        }

        public void setCourse_address(String course_address) {
            this.course_address = course_address;
        }

        public String getCourse_comments() {
            return course_comments;
        }

        public void setCourse_comments(String course_comments) {
            this.course_comments = course_comments;
        }

}
