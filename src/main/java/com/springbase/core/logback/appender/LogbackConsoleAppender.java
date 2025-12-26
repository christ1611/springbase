package com.springbase.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.springbase.core.logback.properties.LogRuleProperties;
import com.springbase.core.logback.filter.LogbackFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static com.springbase.core.logback.LogbackDefine.DEFAULT_APPENDER_NAME_CONSOLE;
import static com.springbase.core.logback.LogbackDefine.ENCODER_PATTERN_CONSOLE;




@Slf4j
public class LogbackConsoleAppender {

    private final LogRuleProperties logRuleProperties;

    public LogbackConsoleAppender(LogRuleProperties logRuleProperties) {
        this.logRuleProperties = logRuleProperties;
    }

    public void setAppender(Logger logger) {
        log.info("[Logback] Console Appender Configuration Start, logger = [{}]", logger.getName());

        String appender = logger.getName() + DEFAULT_APPENDER_NAME_CONSOLE;

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();

        // console log appender 설정
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setName(appender);
        consoleAppender.setContext(loggerContext);

        // JsonLayout 설정
        LogbackLayout logbackLayout = new LogbackLayout();

//        consoleAppender.setEncoder(logbackLayout.setPatternLayout(loggerContext, ENCODER_PATTERN_CONSOLE));

        if(!logRuleProperties.getAppender().isJsonLayout()) {
            consoleAppender.setEncoder(logbackLayout.setPatternLayout(loggerContext, ENCODER_PATTERN_CONSOLE));
        } else {
            consoleAppender.setEncoder(logbackLayout.setJsonLayout());
        }

        // Filter 설정
        if(logRuleProperties.getAppender().isFilter()) {
            LogbackFilter logbackFilter = new LogbackFilter();
            logbackFilter.setContext(loggerContext);
            logbackFilter.start();
            consoleAppender.addFilter(logbackFilter);
        }

        consoleAppender.start();

        // 기존에 add 되어있다면 중복되는 문제. 일단 detach 시킴.
        ((ch.qos.logback.classic.Logger) logger).detachAppender(appender);
        ((ch.qos.logback.classic.Logger) logger).addAppender(consoleAppender);

        log.info("[Logback] Console Appender Configuration End, Set Appender = [{}]", consoleAppender.getName());
    }
}
