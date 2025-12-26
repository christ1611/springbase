package com.springbase.core.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.springbase.core.logback.LogbackDefine;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;



public class LogbackFilter extends Filter<ILoggingEvent> {

    private final LevelFilter levelFilter = new LevelFilter();
    private final ThresholdFilter thresholdFilter = new ThresholdFilter();

    public LogbackFilter() {
        setLevelFilter();
        setThresholdFilter();
    }

    private void setLevelFilter() {
        this.levelFilter.setLevel(Level.DEBUG);
        this.levelFilter.setOnMatch(FilterReply.ACCEPT);
        this.levelFilter.setOnMismatch(FilterReply.DENY);
        this.levelFilter.start();
    }

    private void setThresholdFilter() {
        this.thresholdFilter.setLevel("DEBUG");
        this.thresholdFilter.start();
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        String logFilter = mdcPropertyMap.get("logFilter");
        if (StringUtils.isBlank(logFilter)) { return FilterReply.ACCEPT; }
        String filterType = logFilter.split(":")[0];
        String logLevel = logFilter.split(":")[1];

        switch (LogbackDefine.FilterType.isKindOf(filterType)) {
            case DEFAULT -> {
                return FilterReply.ACCEPT;
            }
            case LEVEL -> {
                levelFilter.setLevel(Level.toLevel(logLevel));
                return levelFilter.decide(event);
            }
            case THRESHOLD -> {
                thresholdFilter.setLevel(logLevel);
                return thresholdFilter.decide(event);
            }
        }
        return FilterReply.DENY;
    }
}