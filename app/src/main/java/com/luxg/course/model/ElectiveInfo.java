package com.luxg.course.model;

import java.io.Serializable;
/**
 * 选课表
 * @author LuXiaogang
 *
 */
public class ElectiveInfo implements Serializable{
	public String studentId ;
	public String courseId ;
	public String teacherId ;
	public float score ;
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "ElectiveInfo [studentId=" + studentId + ", courseId="
				+ courseId + ", teacherId=" + teacherId + ", score=" + score
				+ "]";
	}
	
	
}
