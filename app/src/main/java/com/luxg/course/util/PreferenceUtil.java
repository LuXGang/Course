package com.luxg.course.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户资料\数据工具类
 * <p>
 * 利用ShardPreferences存取用户的资料
 * </p>
 */
public class PreferenceUtil {

    private static String prefName = "luxiaogang";

    /**
     * 由数据的key得到单个用户字符串数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个字符串数据
     */
//    public static String getString(Context context, String prefName, String key) {
//        return key == null ? "" : getPreference(context, prefName).getString(key, "");
//    }
    public static String getStringDefaultValue(Context context, String key, String defaultValue) {
        return key == null ? "" : getPreference(context, prefName).getString(key, defaultValue);
    }
    public static String getString(Context context, String key) {
        return key == null ? "" : getPreference(context, prefName).getString(key, "");
    }

    /**
     * 将用户字符串数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putString(Context context, String prefName, String key, String value) {
        if (key != null) {
            getPreference(context, prefName).edit().putString(key, value).commit();
        }
    }

    public static void putString(Context context, String key, String value) {
        if (key != null) {
            getPreference(context, prefName).edit().putString(key, value).commit();
        }
    }

    /**
     * 由数据的key得到单个用户整型数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个字符串数据
     */
    public static int getInt(Context context, String prefName, String key) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
    }
    public static int getInt(Context context, String prefName, String key, int valueDefault) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, valueDefault);
    }
    public static int getInt(Context context, String key) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
    }
    public static int getInt(Context context, String key, int valueDefault) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, valueDefault);
    }
    /**
     * 将用户整型数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putInt(Context context, String prefName, String key, int value) {
        if (key != null) {
            getPreference(context, prefName).edit().putInt(key, value).commit();
        }
    }

    public static void putInt(Context context, String key, int value) {
        if (key != null) {
            getPreference(context, prefName).edit().putInt(key, value).commit();
        }
    }

    /**
     * 由数据的key得到单个用户浮点型数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个浮点型数据
     */
    public static float getFloat(Context context, String prefName, String key) {
        return key == null ? 0 : getPreference(context, prefName).getFloat(key, 0F);
    }

    public static float getFloat(Context context, String key) {
        return key == null ? 0 : getPreference(context, prefName).getFloat(key, 0F);
    }

    /**
     * 将用户浮点型数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putFloat(Context context, String prefName, String key, float value) {
        if (key != null) {
            getPreference(context, prefName).edit().putFloat(key, value).commit();
        }
    }

    public static void putFloat(Context context, String key, float value) {
        if (key != null) {
            getPreference(context, prefName).edit().putFloat(key, value).commit();
        }
    }

    public static void putLong(Context context, String prefName, String key, long value) {
        if (key != null) {
            getPreference(context, prefName).edit().putLong(key, value).commit();
        }
    }

    public static void putLong(Context context, String key, long value) {
        if (key != null) {
            getPreference(context, prefName).edit().putLong(key, value).commit();
        }
    }

    public static long getLong(Context context, String prefName, String key) {
        return key == null ? 0l : getPreference(context, prefName).getLong(key, 0l);
    }

    public static long getLong(Context context, String key) {
        return key == null ? 0l : getPreference(context, prefName).getLong(key, 0l);
    }

    /**
     * 由数据的key得到单个用户布尔型数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个布尔型数据
     */
    public static boolean getBoolean(Context context, String prefName, String key) {
        return key == null ? false : getPreference(context, prefName).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key) {
        return key == null ? false : getPreference(context, prefName).getBoolean(key, false);
    }
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return key == null ? false : getPreference(context, prefName).getBoolean(key, defaultValue);
    }
    /**
     * 将用户布尔型数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putBoolean(Context context, String prefName, String key, boolean value) {
        if (key != null) {
            getPreference(context, prefName).edit().putBoolean(key, value).commit();
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (key != null) {
            getPreference(context, prefName).edit().putBoolean(key, value).commit();
        }
    }

    /**
     * 删除数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     */
    public static void remove(Context context, String prefName, String key) {
        if (key != null) {
            getPreference(context, prefName).edit().remove(key).commit();
        }
    }

    public static void remove(Context context, String key) {
        if (key != null) {
            getPreference(context, prefName).edit().remove(key).commit();
        }
    }

    /**
     * 清除文件中的所有内容
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     */
    public static void clearAll(Context context, String prefName) {
        getPreference(context, prefName).edit().clear().commit();
    }

    /**
     * 通过上下文对象得到sharePreferences对象
     *
     * @param context  上下文对象
     * @param prefName sharePreferences文件名
     * @return sharePreferences对象
     */
    private static SharedPreferences getPreference(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }
}
