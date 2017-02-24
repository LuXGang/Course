package com.luxg.course.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.luxg.course.widget.basewise.Constant;
import com.umeng.comm.core.sdkmanager.LocationSDKManager;
import com.umeng.community.location.DefaultLocationImpl;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
	private SharedPreferences userInfo;
	private SharedPreferences.Editor editor;
	private Context mContext = null;
	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication mApp;

	public synchronized static MyApplication getInstance() {
		return mApp;
	}

	public static Context getContext() {
		return mApp;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		mContext = this;
		mApp = this;
		getDeviceInfos();
		getLocalPropteis();
		LocationSDKManager.getInstance().addAndUse(new DefaultLocationImpl()) ;
		super.onCreate();
	}


	public void addActivity(Activity activity){
		activityList.add(activity);
	}

	public void exitApp(){
		for(Activity activity : activityList){
			if(activity != null)
				activity.finish();
		}
		System.exit(0);
	}

	/**
	 * 获取本地的配置信息
	 */
	private void getLocalPropteis() {
		// 初始化sharedPreferences
		initSharedPreferences();
		// 读取sharedPreferences中存储的内容
		Constant.studentId = userInfo.getString("studentId","");
		Constant.studentName = userInfo.getString("studentName", "");
		Constant.studentPassword = userInfo.getString("studentPassword", "");
		Constant.studentBirth = userInfo.getString("studentBirth", "");
		Constant.studentClass = userInfo.getString("studentClass", "");
		Constant.studentTime = userInfo.getString("studentTime", "");
		Constant.studentTel = userInfo.getString("studentTel", "");
		Constant.studentSex = userInfo.getString("studentSex", "");
		Constant.avatar = userInfo.getString("avatar", "");
	}

	/**
	 * 初始化
	 */
	private void initSharedPreferences() {
		if (null == userInfo) {
			userInfo = PreferenceUtil.getSharedPreferences(mContext);
		}

		if (null == editor) {
			editor = userInfo.edit();
		}
	}

	/**
	 * 收集手机系统相关的信息
	 */
	private void getDeviceInfos() {
		// 设备号
		Constant.setDeviceId(BaseUtil.getImei(this));
		Constant.setDeviceModel(Build.MODEL);
		// 系统版本
		Constant.setOsVersion(Build.VERSION.RELEASE);
		// 程序版本
		PackageInfo info = BaseUtil.getVersionInfo(this);
		if (info != null) {
			Constant.setVersionCode(info.versionCode);
			Constant.setVersionName(info.versionName);
		}
	}

}
