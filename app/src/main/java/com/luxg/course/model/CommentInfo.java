package com.luxg.course.model;

import java.io.Serializable;
/**
 * 
 * @author LuXiaogang
 *	评论
 */
public class CommentInfo implements Serializable{
	public int commentId;
	public String studentId;
	public String courseId;
	public String teacherId;
	public String commentContent;
	public String commentTime;
	public StudentInfo studentInfo;

	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
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
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}
	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}
	@Override
	public String toString() {
		return "CommentInfo [commentId=" + commentId + ", studentId=" + studentId + ", courseId=" + courseId
				+ ", teacherId=" + teacherId + ", commentContent=" + commentContent + ", commentTime=" + commentTime
				+ ", studentInfo=" + studentInfo + "]";
	}
}
