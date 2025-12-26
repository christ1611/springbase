package com.springbase.core.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.springbase.core.logback.appender.*;
import com.springbase.core.logback.properties.LogRuleProperties;
import com.springbase.core.logback.properties.MinioProperties;
import com.springbase.core.logback.util.MinioUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.zip.GZIPOutputStream;




@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class LogbackManagerImpl implements LogbackManager {

    public static LogbackManagerImpl INSTANCE ;


    @Autowired
    private LogRuleProperties logRuleProperties;
    @Autowired
    private MinioProperties minioProperties;

    private MinioUtil minioUtil;

    @Setter
    private SiftingAppender gidAppender;


    @PostConstruct
    public void init()
    {
        INSTANCE = this;
    }

    public void initLogAppender(Logger logger) {
        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();
        loggerContext.reset();


        // root logger 호출시, log appender 초기화
        if (getLogRuleProperties().getAppender().isConsole()) {
            LogbackConsoleAppender logbackAppender = new LogbackConsoleAppender(logRuleProperties);
            logbackAppender.setAppender(logger); } // Console
        if (getLogRuleProperties().getAppender().isFile()) {
            LogbackFileAppender logbackAppender = new LogbackFileAppender(logRuleProperties);
            logbackAppender.setAppender(logger); } // File
        if (getLogRuleProperties().getAppender().isFileSift()) {
            LogbackFileSiftAppender logbackAppender = new LogbackFileSiftAppender(logRuleProperties);
            logbackAppender.setAppender(logger); } // Sifting
        if (getLogRuleProperties().getAppender().isLogstash()) {
            LogbackLogstashAppender logbackAppender = new LogbackLogstashAppender(logRuleProperties);
            logbackAppender.setAppender(logger); } // Logstash
        if (getLogRuleProperties().getAppender().isKafka()) {
            LogbackKafkaAppender logbackAppender = new LogbackKafkaAppender(logRuleProperties);
            logbackAppender.setAppender(logger); } // Kafka
    }

    public Logger getLogger(Class<?> loggerClass) {
        return getLogger(loggerClass, Level.valueOf(getLogRuleProperties().getLevel().getRoot()));
    }

    public Logger getLogger(Class<?> loggerClass, Level logLevel) {
        try {
            Logger logger = LoggerFactory.getLogger(loggerClass);
            if (logger.getName().equals(Logger.ROOT_LOGGER_NAME)) {
                initLogAppender(logger);
            }
            setLogLevel(logger, logLevel);
            return logger;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Logger getLogger(String loggerName) {
        return getLogger(loggerName, Level.valueOf(getLogRuleProperties().getLevel().getRoot()));
    }

    public Logger getLogger(String loggerName, Level logLevel) {
        try {
            Logger logger = LoggerFactory.getLogger(loggerName);
            if (logger.getName().equals(Logger.ROOT_LOGGER_NAME)) {
                initLogAppender(logger);
            }
            setLogLevel(logger, logLevel);
            return logger;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setLogLevel(Level logLevel) {
        return setLogLevel(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), logLevel);
    }

    public boolean setLogLevel(Logger logger, Level logLevel) {
        try {
            ch.qos.logback.classic.Logger lger = ((ch.qos.logback.classic.Logger) logger);

            if (logger.getName().equals(Logger.ROOT_LOGGER_NAME)) {
                initLogAppender(lger);
                lger.setAdditive(false);
            }
            lger.setLevel(logLevel);
            //System.out.println(lger.getName()+"-"+logLevel.toString()+"-"+lger.isAdditive());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public static void afterMoveLogfile(String service)
    {
        LogbackManagerImpl lm = LogbackManagerImpl.INSTANCE;

        String ymd = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        if( lm.getGidAppender() != null )
        {
            Appender<ILoggingEvent> appender =  lm.getGidAppender().getAppenderTracker().find( service );
            if(appender == null ) return;
            appender.stop();
            lm.getGidAppender().getAppenderTracker().endOfLife( service );


            log.debug("MoveLog [{}]", service);
            String srcPath = lm.getLogRuleProperties().getFiles().getAbsolutePath() +"/"+ymd+"/"
                    + StringUtils.substring(service, 1,4);


            String tgPath = "/hobis/logs/online/"+ ymd+"/" +StringUtils.substring(service, 1,3);

            String srcFile = srcPath + "/" + service + ".log" ;
            String tgFile = tgPath + "/" + service + ".log" ;
            log.debug("SOURCEFILE [{}]", srcFile);
            if(Files.exists( Path.of(srcFile) ) )
            {
                CompletableFuture.runAsync(() -> {
                    try {

                        if(lm.getMinioProperties().isEnable()) {
                            lm.minioUtil = new MinioUtil(lm.getMinioProperties());
                            log.debug("upload log file to minIO" );
                            FileInputStream upInput = new FileInputStream(srcFile);
                            lm.minioUtil.minioUploadFile( "/logs/" + ymd + "/"
                                     + StringUtils.substring(service, 1,4) + "/" + service + ".log"
                                    , upInput
                            );
                        }
                        else {
                            if (!Files.isDirectory(Path.of(tgPath))) {
                                Files.createDirectories(Path.of(tgPath));
                            }

                            Files.write(Path.of(tgFile), Files.readAllBytes(Path.of(srcFile)), StandardOpenOption.CREATE,
                                    StandardOpenOption.APPEND);
                        }


                    }
                    catch ( Exception e)
                    {
                        log.debug("log file copy error", e);
                    }

                });
            }
        }

    }
    public static void afterMoveLogfileGz(String gid)
    {
        LogbackManagerImpl lm = LogbackManagerImpl.INSTANCE;


        if( lm.getGidAppender() != null )
        {
            Appender<ILoggingEvent> appender =  lm.getGidAppender().getAppenderTracker().find( gid );
            if(appender == null ) return;
            appender.stop();
            lm.getGidAppender().getAppenderTracker().endOfLife( gid );


            log.debug("MoveLog [{}]", gid);
            String srcPath = lm.getLogRuleProperties().getFiles().getAbsolutePath()
                    + StringUtils.substring(gid, 0,8);


            String tgPath = "/hobis/logs/online/"+ StringUtils.substring(gid, 0,8);

            String srcFile = srcPath + "/" + gid + ".log" ;
            String tgFile = tgPath + "/" + gid + ".log.gz" ;

            if(Files.exists( Path.of(srcFile) ) )
            {
                CompletableFuture.runAsync(() -> {
                    try {

                        if( ! Files.isDirectory(Path.of(tgPath))) {
                            Files.createDirectories(Path.of(tgPath));
                        }
                        FileInputStream fi = new FileInputStream(srcFile);
                        FileOutputStream fo = new FileOutputStream(tgFile);

                        try (GZIPOutputStream gzLog = new GZIPOutputStream( fo ))
                        {
                            FileCopyUtils.copy(fi, gzLog);
                        }

                        Files.deleteIfExists( Path.of(srcFile) );
                        //Files.move(Path.of(srcFile), Path.of(tgFile), StandardCopyOption.REPLACE_EXISTING);
                    }
                    catch ( Exception e)
                    {
                        log.debug("log file copy error", e);
                    }

                });
            }
        }

    }
}
