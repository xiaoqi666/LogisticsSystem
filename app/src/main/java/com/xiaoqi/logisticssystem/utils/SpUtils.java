package com.xiaoqi.logisticssystem.utils;


import com.xiaoqi.logisticssystem.AppClient;

/**
 * SharedPreferences工具类
 */
public class SpUtils {
    public static void putString(String key, String value) {
        AppClient.getSp().edit().putString(key, value).commit();
    }

    public static void putInt(String key, int value) {
        AppClient.getSp().edit().putInt(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        AppClient.getSp().edit().putBoolean(key, value).commit();
    }

    public static String getString(String key, String value) {
        return AppClient.getSp().getString(key, value);
    }

    public static int getInt(String key, int value) {
        return AppClient.getSp().getInt(key, value);
    }

    public static boolean getBoolean(String key, boolean value) {
        return AppClient.getSp().getBoolean(key, value);
    }


}
