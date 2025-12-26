package com.springbase.core.logback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



@Slf4j
public class LogbackDefine {

    public static String MDC_GLOBAL_ID = "GlobalId";
    public static String MDC_SERVICE = "SVC";

    private static String fileNamePatternPrefix;
    public static String ENCODER_PATTERN_CONSOLE = "%d{yyyy-MM-dd HH:mm:ss}.%-3relative %-22X{"+ MDC_GLOBAL_ID +"} %-5level [%13thread{13}] %-50.50(%C{2}.%M) : %05line| %message%n";
    public static String ENCODER_PATTERN_FILE = "[%-22X{" + MDC_GLOBAL_ID + "}] %d{ISO8601} %-5level [%-13.13thread{13}] %-65.65logger:%05line| %message%n";
    public static String ENCODER_PATTERN_FILE_SIFT = ENCODER_PATTERN_FILE; // SiftingAppender File log Encoder pattern
    public static String DEFAULT_APPENDER_NAME_FILE = "_FLE"; // file appender default name
    public static String DEFAULT_APPENDER_NAME_FILE_SIFT = "_FLS"; // file sift appender default name
    public static String DEFAULT_APPENDER_NAME_LOGSTASH = "_LST"; // file sift appender default name
    public static String DEFAULT_APPENDER_NAME_CONSOLE = "_CSE"; // console appender default name
    public static String FILE_NAME_PATTERN = "1QOnCore"; // File log File Name pattern
    public static String FILE_NAME_PATTERN_SUFFIX = "-%d{yyyy-MM-dd}.%i"; // File log File Name pattern suffix

    public enum AppenderType {
        CONSOLE, FILE, FILE_SHIFT, KAFKA, LOGSTASH
    }

    @Getter
    @RequiredArgsConstructor
    public enum FilterDivision {
        TRM_NO("trmNo"),
        USER_ID("userId");

        private final String filterDivision;
//        private final int order;

        public String getFilterName() {
//            switch (this) {
//                case TRM_NO -> { return SysInfo.getCtxTrmNo(); }
//                case USER_ID -> { return SysInfo.getCtxUserId(); }
//            }
            return null;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum FilterType {
        DEFAULT("default"), LEVEL("level"), THRESHOLD("threshold");

        private final String value;

        public static FilterType isKindOf(String value) {
            for (FilterType eItm : FilterType.values()) {
                if (eItm.value.equals(value)) { return eItm; }
            }
            return null;
        }
    }

    public static String getFileNamePrefix() {
        return fileNamePatternPrefix;
    }

    public static void setFileNamePrefix(String configLogFileAbsolutePath) {
        if(StringUtils.isEmpty(fileNamePatternPrefix) && StringUtils.isNotBlank(configLogFileAbsolutePath)) {
            fileNamePatternPrefix = configLogFileAbsolutePath+"/";
        }
        log.info("[Logback] Set File name prefix = [{}]", fileNamePatternPrefix);
    }
}
