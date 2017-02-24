package com.luxg.course.model;

import java.io.Serializable;
/**
 * 学生头像
 * @author LuXiaogang
 *
 */
public class StudentImageInfo implements Serializable{
	public String studentId;
	public String imagePath;
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	public String toString() {
		return "StudentImageInfo [studentId=" + studentId + ", imagePath="
				+ imagePath + "]";
	}
	
}
