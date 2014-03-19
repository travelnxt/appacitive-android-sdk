package com.appacitive.java;

import com.appacitive.core.interfaces.LogLevel;
import com.appacitive.core.interfaces.Logger;

import java.util.logging.Level;

/**
 * Created by sathley.
 */
public class JavaLogger implements com.appacitive.core.interfaces.Logger {
    private static LogLevel logLevel = LogLevel.INFO;
    private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Logger.TAG);


    @Override
    public void setLogLevel(LogLevel logLevel) {
        JavaLogger.logLevel = logLevel;
    }

    @Override
    public void error(String message) {
        if (JavaLogger.logLevel.ordinal() <= LogLevel.ERROR.ordinal())
            LOGGER.log(Level.SEVERE, message);
    }

    @Override
    public void info(String message) {
        if (JavaLogger.logLevel.ordinal() <= LogLevel.INFO.ordinal())
            LOGGER.log(Level.INFO, message);
    }

    @Override
    public void verbose(String message) {
        if (JavaLogger.logLevel.ordinal() <= LogLevel.VERBOSE.ordinal())
            LOGGER.log(Level.FINEST, message);
    }

    @Override
    public void warn(String message) {
        if (JavaLogger.logLevel.ordinal() <= LogLevel.WARN.ordinal())
            LOGGER.log(Level.WARNING, message);
    }
}
