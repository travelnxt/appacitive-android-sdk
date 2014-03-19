package com.appacitive.core.interfaces;

/**
 * Created by sathley.
 */
public interface Logger {

    public String TAG = "APPACITIVE";

    public void setLogLevel(LogLevel logLevel);



    public void error(String message);

    public void info(String message);

    public void verbose(String message);

    public void warn(String message);
}
