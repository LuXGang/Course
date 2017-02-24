package com.luxg.course.model;

import java.io.Serializable;
/**
 * 教师表
 * @author LuXiaogang
 *
 */
public class TeacherInfo implements Serializable{
	public String teacherId;
	public String teacherName;
	public String teacherDepartment;
	public String teacherBirth;
	public String teacherSex;
	public String teacherTel;
	public String teacherPassword;
	public String teacherPost;//职称
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherDepartment() {
		return teacherDepartment;
	}
	public void setTeacherDepartment(String teacherDepartment) {
		this.teacherDepartment = teacherDepartment;
	}
	public String getTeacherBirth() {
		return teacherBirth;
	}
	public void setTeacherBirth(String teacherBirth) {
		this.teacherBirth = teacherBirth;
	}
	public String getTeacherSex() {
		return teacherSex;
	}
	public void setTeacherSex(String teacherSex) {
		this.teacherSex = teacherSex;
	}
	public String getTeacherTel() {
		return teacherTel;
	}
	public void setTeacherTel(String teacherTel) {
		this.teacherTel = teacherTel;
	}
	public String getTeacherPassword() {
		return teacherPassword;
	}
	public void setTeacherPassword(String teacherPassword) {
		this.teacherPassword = teacherPassword;
	}
	public String getTeacherPost() {
		return teacherPost;
	}
	public void setTeacherPost(String teacherPost) {
		this.teacherPost = teacherPost;
	}
	@Override
	public String toString() {
		return "TeacherInfo [teacherId=" + teacherId + ", teacherName="
				+ teacherName + ", teacherDepartment=" + teacherDepartment
				+ ", teacherBirth=" + teacherBirth + ", teacherSex="
				+ teacherSex + ", teacherTel=" + teacherTel
				+ ", teacherPassword=" + teacherPassword + ", teacherPost="
				+ teacherPost + "]";
	}
	
	
}
