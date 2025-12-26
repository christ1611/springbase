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
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.Logger;

import static com.springbase.core.logback.LogbackDefine.*;




@Slf4j
public class LogbackLogstashAppender {

    private final LogRuleProperties logRuleProperties;

    public LogbackLogstashAppender(LogRuleProperties logRuleProperties) {
        this.logRuleProperties = logRuleProperties;
    }

    public void setAppender(Logger logger) {
        log.info("[Logback] Logstash Appender Configuration Start, logger = [{}]", logger.getName());

        String appender = logger.getName() + DEFAULT_APPENDER_NAME_LOGSTASH;
        String fileName = LogbackDefine.getFileNamePrefix() + FILE_NAME_PATTERN + "-json";

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();

        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.start();

        // file log appender 설정
        RollingFileAppender<ILoggingEvent> rollFileAppender = new RollingFileAppender<>();
        rollFileAppender.setName(appender);
        rollFileAppender.setContext(loggerContext);
        rollFileAppender.setEncoder(logstashEncoder);
        rollFileAppender.setAppend(true);
        rollFileAppender.setPrudent(false);
        rollFileAppender.setFile(fileName + ".log");

        // file log policy 설정
        TimeBasedRollingPolicy<ILoggingEvent> timeRollPolicy = new TimeBasedRollingPolicy<>();
        timeRollPolicy.setContext(loggerContext);
        timeRollPolicy.setFileNamePattern(fileName + FILE_NAME_PATTERN_SUFFIX + ".log");
        timeRollPolicy.setParent(rollFileAppender);

        // file max size 설정
        SizeAndTimeBasedFNATP<ILoggingEvent> sizeBased = new SizeAndTimeBasedFNATP<>();
        sizeBased.setContext(loggerContext);
        sizeBased.setMaxFileSize(FileSize.valueOf(logRuleProperties.getFiles().getMaxSize()));
        sizeBased.setTimeBasedRollingPolicy(timeRollPolicy);
        timeRollPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeBased);
        timeRollPolicy.setMaxHistory(3);
        timeRollPolicy.start();

        rollFileAppender.setRollingPolicy(timeRollPolicy);
        rollFileAppender.start();

        // 기존에 add 되어있다면 중복되는 문제. 일단 detach 시킴.
        ((ch.qos.logback.classic.Logger) logger).detachAppender(appender);
        ((ch.qos.logback.classic.Logger) logger).addAppender(rollFileAppender);

        log.info("[Logback] Logstash Appender Configuration End, Set Appender = [{}]", rollFileAppender.getName());
    }
}
