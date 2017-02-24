package com.luxg.course.model;

import java.io.Serializable;
/**
 * 
 * @author LuXiaogang
 * 课程
 *	android端实体类要根据这个来改
 */
public class CourseInfo implements Serializable{
	public String courseId;
	public String courseName;
	public float courseCredit;//学分
	public String courseTimeDay;//上课时间 传入1 2 3 4 代表周几 需要
	public String courseTime;//上课时间 传入1 2 3 4 代表第几大节课
	public String courseAddress;
	public String courseNumber;
	public String courseHour;
	public String courseIntroduce;
	public String courseBook;
	public String courseVideo;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public float getCourseCredit() {
		return courseCredit;
	}
	public void setCourseCredit(float courseCredit) {
		this.courseCredit = courseCredit;
	}
	public String getCourseTimeDay() {
		return courseTimeDay;
	}
	public void setCourseTimeDay(String courseTimeDay) {
		this.courseTimeDay = courseTimeDay;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getCourseAddress() {
		return courseAddress;
	}
	public void setCourseAddress(String courseAddress) {
		this.courseAddress = courseAddress;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getCourseHour() {
		return courseHour;
	}
	public void setCourseHour(String courseHour) {
		this.courseHour = courseHour;
	}
	public String getCourseIntroduce() {
		return courseIntroduce;
	}
	public void setCourseIntroduce(String courseIntroduce) {
		this.courseIntroduce = courseIntroduce;
	}
	public String getCourseBook() {
		return courseBook;
	}
	public void setCourseBook(String courseBook) {
		this.courseBook = courseBook;
	}
	public String getCourseVideo() {
		return courseVideo;
	}
	public void setCourseVideo(String courseVideo) {
		this.courseVideo = courseVideo;
	}

	@Override
	public String toString() {
		return "CourseInfo{" +
				"courseId='" + courseId + '\'' +
				", courseName='" + courseName + '\'' +
				", courseCredit=" + courseCredit +
				", courseTimeDay='" + courseTimeDay + '\'' +
				", courseTime='" + courseTime + '\'' +
				", courseAddress='" + courseAddress + '\'' +
				", courseNumber='" + courseNumber + '\'' +
				", courseHour='" + courseHour + '\'' +
				", courseIntroduce='" + courseIntroduce + '\'' +
				", courseBook='" + courseBook + '\'' +
				", courseVideo='" + courseVideo + '\'' +
				'}';
	}
}
