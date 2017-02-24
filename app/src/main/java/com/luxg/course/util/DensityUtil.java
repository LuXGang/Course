package com.luxg.course.util;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by LuXiaogang on 2016/2/25.
 */
public class DensityUtil {

    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int widthInDp = px2dip(context, height);
        return widthInDp;
    }


    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }







    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕的宽
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels - getStatusBarHeight(context);
    }

    /**
     * 用反射方法获取系统状态栏的高度
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class cls = Class.forName("com.android.internal.R$dimen");
            Object obj = cls.newInstance();
            Field field = cls.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }



}
