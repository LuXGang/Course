package com.luxg.course.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LuXiaogang on 2016/4/13.
 */
public class BaseUtil {
    public static boolean isPassword(String password) {
        String regex = "^[0-9a-zA-Z_.,*/~!?@#￥%^&*(){}<>+-]{6,20}$";
        return startCheck(regex, password);
    }

    public static PackageInfo getVersionInfo(Context context) {
        PackageInfo info = null;
        String versionCode = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 获取系统的IMEI号
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = telephonyManager.getDeviceId();
        return id;
    }

    private static boolean startCheck(String reg, String string) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
