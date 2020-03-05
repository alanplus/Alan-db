package com.alan.db;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Alan
 * 时 间：2020-03-05
 * 简 述：<功能简述>
 */
public class LogUtil {

    public static final String TAG = "alan_db";

    public static void error(Exception e) {
        if (Resources.getSystem().getBoolean(R.bool.is_debug)) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public static void d(String text) {
        if (Resources.getSystem().getBoolean(R.bool.is_debug) && !TextUtils.isEmpty(text)) {
            Log.d(TAG, text);
        }
    }
}
