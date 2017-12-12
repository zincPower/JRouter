package com.zinc.librouter.utils;

import android.util.Log;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/11
 * @description
 */

public class RLog {

    private static final String TAG = "JRouter";
    private static boolean sLoggable = false;

    public static void showLog(boolean sLoggable){
        RLog.sLoggable = sLoggable;
    }

    public static void i(String msg){
        if(sLoggable) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg){
        if(sLoggable) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg){
        if(sLoggable){
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if(sLoggable){
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable throwable){
        if(sLoggable){
            Log.e(TAG, msg, throwable);
        }
    }

}
