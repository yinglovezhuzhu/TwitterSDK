package com.owen.twitter.sdk.utils;

import android.util.Log;

import com.owen.twitter.sdk.SDKVersion;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 日志打印工具类
 * <br/>Author：yunying.zhang
 * <br/>Email: yunyingzhang@rastar.com
 * <br/>Date: 2018/12/21
 */
public class LogUtils {

    private static boolean DEBUG = true;

    private static final String LOG_TAG = "TwitterSDK_" + SDKVersion.SDK_VERSION;


    public static void setDebugMode(boolean debugMode) {
        DEBUG = debugMode;
    }

    public static void v(String tag, String msg) {
        if(DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if(DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if(DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if(DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void v(String msg) {
        if(DEBUG) {
            Log.v(LOG_TAG, msg);
        }
    }

    public static void d(String msg) {
        if(DEBUG) {
            Log.d(LOG_TAG, msg);
        }
    }

    public static void i(String msg) {
        if(DEBUG) {
            Log.i(LOG_TAG, msg);
        }
    }

    public static void w(String msg) {
        if(DEBUG) {
            Log.w(LOG_TAG, msg);
        }
    }

    public static void e(String msg) {
        if(DEBUG) {
            Log.e(LOG_TAG, msg);
        }
    }


    /**
     * 获取异常信息的堆栈内容
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        String error = writer.toString();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;

    }

}
