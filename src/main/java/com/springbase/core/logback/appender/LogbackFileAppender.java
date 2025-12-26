package com.springbase.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.springbase.core.logback.properties.LogRuleProperties;
import com.springbase.core.logback.LogbackDefine;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static com.springbase.core.logback.LogbackDefine.*;



@Slf4j
public class LogbackFileAppender {
    private final LogRuleProperties logRuleProperties;

    public LogbackFileAppender(LogRuleProperties logRuleProperties) {
        this.logRuleProperties = logRuleProperties;
    }

    public void setAppender(Logger logger) {
        log.info("[Logback] File Appender Configuration Start, logger = [{}]", logger.getName());

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();

        String appender = logger.getName() + DEFAULT_APPENDER_NAME_FILE;
        String filename = LogbackDefine.getFileNamePrefix() + FILE_NAME_PATTERN + DEFAULT_APPENDER_NAME_FILE;

        // file log encoder 및 appender 설정
        LogbackLayout logbackLayout = new LogbackLayout();
        RollingFileAppender<ILoggingEvent> rollFileAppender = new RollingFileAppender<>();
        rollFileAppender.setName(appender);
        rollFileAppender.setContext(loggerContext);
        rollFileAppender.setEncoder(logbackLayout.setPatternLayout(loggerContext, ENCODER_PATTERN_FILE));
        rollFileAppender.setAppend(true); // restart 시 기존파일에 추가 (setAppend: true)
        rollFileAppender.setPrudent(false); // file 저장시 lock 생성하여 처리 (setPrudent: true)
        rollFileAppender.setFile(filename + ".log");

        // file log policy 설정
        TimeBasedRollingPolicy<ILoggingEvent> timeRollPolicy = new TimeBasedRollingPolicy<>();
        timeRollPolicy.setContext(loggerContext);
        timeRollPolicy.setFileNamePattern(filename + FILE_NAME_PATTERN_SUFFIX + ".log");
        timeRollPolicy.setParent(rollFileAppender);

        // file max size 설정
        SizeAndTimeBasedFNATP<ILoggingEvent> sizeBased = new SizeAndTimeBasedFNATP<>();
        sizeBased.setContext(loggerContext);
        sizeBased.setMaxFileSize(FileSize.valueOf(logRuleProperties.getFiles().getMaxSize()));
        sizeBased.setTimeBasedRollingPolicy(timeRollPolicy);
        timeRollPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeBased);
        timeRollPolicy.start();

        rollFileAppender.setRollingPolicy(timeRollPolicy);
        rollFileAppender.start();

        // 기존에 add 되어있다면 중복되는 문제. 일단 detach 시킴.
        ((ch.qos.logback.classic.Logger) logger).detachAppender(logger.getName() + DEFAULT_APPENDER_NAME_FILE);
        ((ch.qos.logback.classic.Logger) logger).addAppender(rollFileAppender);

        log.info("[Logback] File Appender Configuration End, Set Appender = [{}]", rollFileAppender.getName());
    }
}
