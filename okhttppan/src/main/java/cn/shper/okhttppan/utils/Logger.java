package cn.shper.okhttppan.utils;

import android.util.Log;

import cn.shper.okhttppan.BuildConfig;

/**
 * author shper
 * description TODO
 * version 0.1 16-6-3 C 创建
 */
public class Logger {

    private static final String TAG = "OkHttpPan";

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

}
