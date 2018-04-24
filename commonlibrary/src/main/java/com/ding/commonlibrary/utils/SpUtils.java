package com.ding.commonlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * @author ding
 */
public class SpUtils {
    /**
     * SharedPreferences 的名字
     */
    private final static String SP_NAME = "config";
    private SharedPreferences sp;

    private static SpUtils mInstance;

    private SpUtils(Context context) {
        sp = context.getSharedPreferences(SP_NAME, 0);
    }

    public static SpUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SpUtils.class) {
                if (mInstance == null) {
                    mInstance = new SpUtils(context);
                }
            }
        }
        return mInstance;
    }

    public void saveBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public void saveInt(String key, int value) {

        sp.edit().putInt(key, value).apply();
    }

    public void saveString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }


    public void saveSet(String key, Set<String> value) {
        sp.edit().putStringSet(key, value).apply();
    }

    public int getInt(String key, int defValue) {

        return sp.getInt(key, defValue);

    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean getBoolean(String key,
                              boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public Set<String> getSet(String key, Set<String> defValue) {
        return sp.getStringSet(key, defValue);
    }

    public void removeValue(String value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(value);
        edit.apply();
    }

    public void clear() {
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }
}
