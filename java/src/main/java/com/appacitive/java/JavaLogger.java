package com.appacitive.java;

import com.appacitive.core.interfaces.LogLevel;

import java.util.logging.Level;

/**
* Created by sathley.
*/
public class JavaLogger implements com.appacitive.core.interfaces.Logger {
    private LogLevel logLevel = LogLevel.ERROR;
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("APPACITIVE");


    @Override
    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void Assert(String message) {
        LOGGER.log(Level.FINE, message);
    }

    @Override
    public void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    @Override
    public void info(String message) {
        LOGGER.log(Level.INFO, message);
    }

    @Override
    public void verbose(String message) {
        LOGGER.log(Level.FINEST, message);
    }

    @Override
    public void warn(String message) {
        LOGGER.log(Level.WARNING, message);
    }
}
