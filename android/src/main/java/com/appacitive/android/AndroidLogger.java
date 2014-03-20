package com.appacitive.android;

import android.util.Log;
import com.appacitive.core.interfaces.LogLevel;
import com.appacitive.core.interfaces.Logger;

/**
 * Created by sathley.
 */
public class AndroidLogger implements com.appacitive.core.interfaces.Logger {
    private static final String TAG = Logger.TAG;

    private volatile static LogLevel logLevel = LogLevel.INFO;

    @Override
    public synchronized void setLogLevel(LogLevel logLevel) {
        AndroidLogger.logLevel = logLevel;
    }

    @Override
    public void error(String message) {
        if (AndroidLogger.logLevel.ordinal() <= LogLevel.ERROR.ordinal())
            Log.e(TAG, message);
    }

    @Override
    public void info(String message) {
        if (AndroidLogger.logLevel.ordinal() <= LogLevel.INFO.ordinal())
            Log.i(TAG, message);
    }

    @Override
    public void verbose(String message) {
        if (AndroidLogger.logLevel.ordinal() <= LogLevel.VERBOSE.ordinal())
            Log.v(TAG, message);
    }

    @Override
    public void warn(String message) {
        if (AndroidLogger.logLevel.ordinal() <= LogLevel.WARN.ordinal())
            Log.w(TAG, message);
    }
}
