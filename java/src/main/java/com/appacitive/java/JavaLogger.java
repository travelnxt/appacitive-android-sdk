package com.appacitive.java;

import com.appacitive.core.interfaces.LogLevel;
import com.appacitive.core.interfaces.Logger;

import java.util.logging.Level;

/**
 * Created by sathley.
 */
public class JavaLogger implements com.appacitive.core.interfaces.Logger {

    private static class Instance {
        private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Logger.TAG);
        private volatile static LogLevel logLevel = LogLevel.INFO;


        public static void setLogLevel(LogLevel logLevel) {
            Instance.logLevel = logLevel;
        }

        public static void error(String message) {
            if (Instance.logLevel.ordinal() <= LogLevel.ERROR.ordinal())
                LOGGER.log(Level.SEVERE, message);
        }

        public static void info(String message) {
            if (Instance.logLevel.ordinal() <= LogLevel.INFO.ordinal())
                LOGGER.log(Level.INFO, message);
        }

        public static void verbose(String message) {
            if (Instance.logLevel.ordinal() <= LogLevel.VERBOSE.ordinal())
                LOGGER.log(Level.FINEST, message);
        }

        public static void warn(String message) {
            if (Instance.logLevel.ordinal() <= LogLevel.WARN.ordinal())
                LOGGER.log(Level.WARNING, message);
        }
    }

    @Override
    public synchronized void setLogLevel(LogLevel logLevel) {
        Instance.setLogLevel(logLevel);
    }

    @Override
    public void error(String message) {
        Instance.error(message);
    }

    @Override
    public void info(String message) {
        Instance.info(message);
    }

    @Override
    public void verbose(String message) {
        Instance.verbose(message);
    }

    @Override
    public void warn(String message) {
        Instance.warn(message);
    }
}
