package com.luxg.course.model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 学生表
 * @author LuXiaogang
 *
 */
public class StudentInfo implements Serializable{
	public String studentId;
	public String studentName;
	public String studentBirth;
	public String studentClass;//班级
	public String studentTel;
	public String studentPassword;
	public String studentTime;//年级
	public String studentSex;
	public ArrayList<StudentImageInfo> studentImageInfo;
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentBirth() {
		return studentBirth;
	}
	public void setStudentBirth(String studentBirth) {
		this.studentBirth = studentBirth;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public String getStudentTel() {
		return studentTel;
	}
	public void setStudentTel(String studentTel) {
		this.studentTel = studentTel;
	}
	public String getStudentPassword() {
		return studentPassword;
	}
	public void setStudentPassword(String studentPassword) {
		this.studentPassword = studentPassword;
	}
	public String getStudentTime() {
		return studentTime;
	}
	public void setStudentTime(String studentTime) {
		this.studentTime = studentTime;
	}
	public String getStudentSex() {
		return studentSex;
	}
	public void setStudentSex(String studentSex) {
		this.studentSex = studentSex;
	}
	public ArrayList<StudentImageInfo> getStudentImageInfo() {
		return studentImageInfo;
	}
	public void setStudentImageInfo(ArrayList<StudentImageInfo> studentImageInfo) {
		this.studentImageInfo = studentImageInfo;
	}
	@Override
	public String toString() {
		return "StudentInfo [studentId=" + studentId + ", studentName="
				+ studentName + ", studentBirth=" + studentBirth
				+ ", studentClass=" + studentClass + ", studentTel="
				+ studentTel + ", studentPassword=" + studentPassword
				+ ", studentTime=" + studentTime + ", studentSex=" + studentSex
				+ ", studentImageInfo=" + studentImageInfo + "]";
	}
	
}
