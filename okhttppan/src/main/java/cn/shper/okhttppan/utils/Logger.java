package cn.shper.okhttppan.utils;

import android.util.Log;

import java.util.Locale;

import cn.shper.okhttppan.BuildConfig;
import cn.shper.okhttppan.OkHttpPan;

/**
 * author shper
 * description 日志输出类
 * version 0.1 16-6-3 C 创建
 */
public class Logger {

    /** This is a log tag and it also contains the source of a log message. */
    private static final String TAG = "OkHttpPan - %1$s.%2$s(L:%3$d)";

    /**
     * Private constructor, avoid this class wall be instantiated.
     */
    private Logger() {
    }

    /**
     * Send a verbose log message.
     *
     * @param messages These messages you would like logged.
     */
    public static void v(String... messages) {
        Log.v(generateTag(), concatMessage(messages));
    }

    /**
     * Send a debug log message.
     *
     * @param messages These messages you would like logged.
     */
    public static void d(String... messages) {
        if (OkHttpPan.getInstance().isDebug) {
            Log.d(generateTag(), concatMessage(messages));
        }
    }

    /**
     * Send a info log message.
     *
     * @param messages These messages you would like logged.
     */
    public static void i(String... messages) {
        Log.i(generateTag(), concatMessage(messages));
    }

    /**
     * Send a warning log message.
     *
     * @param messages These messages you would like logged.
     */
    public static void w(String... messages) {
        Log.w(generateTag(), concatMessage(messages));
    }

    /**
     * Send a error log message.
     *
     * @param messages These messages you would like logged.
     */
    public static void e(String... messages) {
        Log.e(generateTag(), concatMessage(messages));
    }

    /**
     * Returns the log tag,
     * the tag contains the caller class name, method name, and source line number.
     */
    private static String generateTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format(Locale.getDefault(),
                TAG, callerClazzName, caller.getMethodName(), caller.getLineNumber());
    }

    /**
     * This method is used to concat all of the log message.
     *
     * @param messages These messages you would like logged.
     * @return The string representation of the data in this messages.
     */
    private static String concatMessage(String... messages) {
        if (null == messages || messages.length < 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message);
        }
        return sb.toString();
    }

}
