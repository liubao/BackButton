package com.liubao.backbutton;

import android.content.SharedPreferences;

/**
 * * Created by liubao on 2018/6/29.
 */
public class SharedPreferencesUtils {
    private static final String PREF_FILE = "back_button";

    public static void putInt(String key, int value) {
        getEditor(PREF_FILE).putInt(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        getEditor(PREF_FILE).putFloat(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        try {
            return getSharedPreferences(PREF_FILE).getInt(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }


    public static float getFloat(String key, float defValue) {
        try {
            return getSharedPreferences(PREF_FILE).getFloat(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    public static void putString(String key, String value) {
        getEditor(PREF_FILE).putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return getSharedPreferences(PREF_FILE).getString(key, defValue);
    }

    public static void putString(String name, String key, String value) {
        getEditor(name).putString(key, value).apply();
    }

    public static void getString(String name, String key, String defValue) {
        getSharedPreferences(name).getString(key, defValue);
    }

    private static SharedPreferences.Editor getEditor(String name) {
        return getSharedPreferences(name).edit();
    }


    private static SharedPreferences getSharedPreferences(String name) {
        return MainApplication.context.getSharedPreferences(name, 0);
    }
}
