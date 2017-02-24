package com.luxg.course.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 授课表  哪位教师教哪门课
 * @author LuXiaogang
 *
 */
public class TeachingInfo implements Serializable{
	public String teacherId;
	public String courseId;
	public CourseInfo course;
	public TeacherInfo teacher;
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public CourseInfo getCourse() {
		return course;
	}

	public void setCourse(CourseInfo course) {
		this.course = course;
	}

	public TeacherInfo getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherInfo teacher) {
		this.teacher = teacher;
	}

	@Override
	public String toString() {
		return "TeachingInfo{" +
				"teacherId='" + teacherId + '\'' +
				", courseId='" + courseId + '\'' +
				", course=" + course +
				", teacher=" + teacher +
				'}';
	}
}
