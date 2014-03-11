package com.appacitive.android;

import android.util.Log;
import com.appacitive.core.interfaces.LogLevel;

/**
 * Created by sathley.
 */
public class AndroidLogger implements com.appacitive.core.interfaces.Logger {
    private static final String TAG = "APPACITIVE";

    @Override
    public void setLogLevel(LogLevel logLevel) {
    }

    @Override
    public void Assert(String message) {
        Log.v(TAG, message);
    }

    @Override
    public void error(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void info(String message) {
        Log.i(TAG, message);
    }

    @Override
    public void verbose(String message) {
        Log.v(TAG, message);
    }

    @Override
    public void warn(String message) {
        Log.w(TAG, message);
    }
}
