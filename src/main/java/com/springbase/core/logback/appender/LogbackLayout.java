package com.springbase.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.fieldnames.LogstashFieldNames;
import java.nio.charset.Charset;



public class LogbackLayout {

    public PatternLayoutEncoder setPatternLayout(LoggerContext loggerContext, String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(pattern);
        encoder.setCharset(Charset.defaultCharset());
        encoder.start();
        return encoder;
    }

    public LogstashEncoder setJsonLayout() {
        LogstashFieldNames logstashFieldNames = new LogstashFieldNames();
        logstashFieldNames.setCallerClass("class");
        logstashFieldNames.setCallerMethod("method");
        logstashFieldNames.setCallerLine("line");
        logstashFieldNames.setCallerFile("file_name");

        LogstashEncoder encoder = new LogstashEncoder();
        encoder.setFieldNames(logstashFieldNames);
        encoder.setIncludeCallerData(true);
        encoder.start();
        return encoder;
    }
}