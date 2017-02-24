package com.luxg.course.widget.basewise;

import android.app.Activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Constant {
	//跟网络有关的url
	public static final String URL = "http://10.20.3.81:8080/Mycourseproject/";


    // 个人登录信息
	public static String studentId ="";
	public static String studentName ="";
	public static String studentBirth;
	public static String studentClass;
	public static String studentTel ="";
	public static String studentPassword;
	public static String studentTime;
	public static String studentSex;
	public static String avatar;


	// 手机系统信息
	private static String deviceId;// 设备序列号
	private static String deviceModel;// 手机型号
	private static String osVersion;// 手机系统版本
	// 程序版本信息
	private static String versionName;
	private static int versionCode = 0;

	public static String getDeviceId() {
		return deviceId;
	}

	public static void setDeviceId(String deviceId) {
		Constant.deviceId = deviceId;
	}

	public static String getDeviceModel() {
		return deviceModel;
	}

	public static void setDeviceModel(String deviceModel) {
		Constant.deviceModel = deviceModel;
	}

	public static String getOsVersion() {
		return osVersion;
	}

	public static void setOsVersion(String osVersion) {
		Constant.osVersion = osVersion;
	}

	public static String getVersionName() {
		return versionName;
	}

	public static void setVersionName(String versionName) {
		Constant.versionName = versionName;
	}

	public static int getVersionCode() {
		return versionCode;
	}

	public static void setVersionCode(int versionCode) {
		Constant.versionCode = versionCode;
	}

	public static List<Activity> activityList01 = new ArrayList<Activity>();

	public static boolean isLogin() {
		return !"".equals(Constant.studentId);
	}

	private static LinkedList<Activity> activityList = new LinkedList<Activity>();


	public static void remove() {
		Activity activity = activityList.getFirst();
		if (activity != null)
			activityList.remove();
	}



	/**
	 * 退出程序(关闭所有当前运行程序所有的Activity)
	 */
	public static void killApp01() {

		int siz = activityList01.size();
		for (int i = 0; i < siz; i++) {
			if (activityList01.get(i) != null) {
				((Activity) activityList01.get(i)).finish();
			}
		}
	}

	/**
	 * 退出程序(关闭所有当前运行程序所有的Activity)
	 */
	public static void killApp() {
		int siz = activityList.size();
		for (int i = 0; i < siz; i++) {
			if (activityList.get(i) != null) {
				(activityList.get(i)).finish();
			}
		}
		System.exit(0);   //正常退出
	}


	/**
	 * 连接网络结束后状态
	 */
	public enum NetworkFeedback {
		SUCCESS("成功"), NETWORK_FAILED("数据异常，请确保网络通畅"), DATA_ERROR("数据错误"), NO_DATA(
				"没有搜索到相关数据"), NO_MORE_DATA("已至最后一页");

		private String mValue;

		NetworkFeedback(String value) {
			this.mValue = value;
		}

		public String getValue() {
			return this.mValue;
		}
	}




}
