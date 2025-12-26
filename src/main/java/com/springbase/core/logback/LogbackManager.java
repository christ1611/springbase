package com.springbase.core.logback;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;



public interface LogbackManager {
    Logger getLogger(Class<?> loggerClass);
    Logger getLogger(Class<?> loggerClass, Level logLevel);
    Logger getLogger(String loggerName);
    Logger getLogger(String loggerName, Level logLevel);
    boolean setLogLevel(Level logLevel);
    boolean setLogLevel(Logger logger, Level logLevel);
    void initLogAppender(Logger logger);
}
