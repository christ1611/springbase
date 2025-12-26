package com.springbase.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.sift.MDCBasedDiscriminator;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.rolling.*;
import ch.qos.logback.core.sift.AppenderFactory;
import ch.qos.logback.core.util.FileSize;
import com.springbase.core.logback.LogbackManagerImpl;
import com.springbase.core.logback.properties.LogRuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.springbase.core.logback.LogbackDefine.*;




@Slf4j
public class LogbackFileSiftAppender {

    private final LogRuleProperties logRuleProperties;



    public LogbackFileSiftAppender(LogRuleProperties logRuleProperties) {
        this.logRuleProperties = logRuleProperties;
    }

    public void setAppender(Logger logger) {
        log.info("[Logback] FileSift Appender Configuration Start, logger = [{}]", logger.getName());

        String appender = logger.getName() + DEFAULT_APPENDER_NAME_FILE_SIFT;

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger)logger).getLoggerContext();

        SiftingAppender siftingAppender = new SiftingAppender();

        /* File 분리를 위한 Discriminator 설정 */
        MDCBasedDiscriminator mdcBasedDiscriminator = new MDCBasedDiscriminator();
        mdcBasedDiscriminator.setContext(loggerContext);
        mdcBasedDiscriminator.setKey(MDC_SERVICE);
        mdcBasedDiscriminator.setDefaultValue("none");
        mdcBasedDiscriminator.start();

        siftingAppender.setDiscriminator(mdcBasedDiscriminator);
        siftingAppender.setName(appender);
        siftingAppender.setContext(loggerContext);

        AppenderFactory<ILoggingEvent> appenderFactory = new AppenderFactory<ILoggingEvent>() {
            @Override
            public Appender<ILoggingEvent> buildAppender(Context context, String discriminatingValue) throws JoranException {
                String ymd = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
                String filePath = logRuleProperties.getFiles().getAbsolutePath()+"/"
                        + ymd + "/"
                        + StringUtils.substring(discriminatingValue, 1,4) + "/";
                log.debug("FILEPATH TEMP [{}]", filePath);
                try {
                    Files.createDirectories(Path.of(filePath));
                } catch (Exception e) {
                    log.error("Failed to create log directory", e);
                }

                RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
                appender.setName("ROLL_" + discriminatingValue);
                appender.setContext(loggerContext);
                appender.setAppend(true);

                String activeLogFile = filePath + discriminatingValue + ".log";
                appender.setFile(activeLogFile);

                // encoder
                PatternLayoutEncoder encoder = new PatternLayoutEncoder();
                encoder.setContext(loggerContext);
                encoder.setPattern(ENCODER_PATTERN_FILE_SIFT);
                encoder.setCharset(StandardCharsets.UTF_8);
                encoder.start();

                appender.setEncoder(encoder);

                // correct rolling policy
                SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy =
                        new SizeAndTimeBasedRollingPolicy<>();

                policy.setContext(loggerContext);
                policy.setParent(appender);

                // rolled files pattern
                policy.setFileNamePattern(
                        filePath + discriminatingValue + ".%d{yyyy-MM-dd}.%i.log.gz"
                );

                policy.setMaxFileSize(FileSize.valueOf(logRuleProperties.getFiles().getMaxSize()));
                policy.setMaxHistory(logRuleProperties.getFiles().getMaxHistory());

                policy.start();
                appender.setRollingPolicy(policy);

                appender.start();

                return appender;
            }

        };


        siftingAppender.setAppenderFactory(appenderFactory);
        siftingAppender.start();



        ((ch.qos.logback.classic.Logger) logger).detachAppender(appender);
        ((ch.qos.logback.classic.Logger) logger).addAppender(siftingAppender);

        LogbackManagerImpl.INSTANCE.setGidAppender( siftingAppender );
        log.info("[Logback] FileSift Appender Configuration End, Set Appender = [{}]", siftingAppender.getName());
    }
}
