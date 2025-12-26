package com.springbase.core.config;

import ch.qos.logback.classic.Level;
import com.springbase.core.logback.LogbackDefine;
import com.springbase.core.logback.LogbackManager;
import com.springbase.core.logback.properties.LogRuleProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(LogRuleProperties.class)
//@ComponentScan(basePackages = {"com.oneqoncore", "com.inobis"})
public class LogbackConfig {

    private final LogRuleProperties logRuleProperties;
    private final LogbackManager logbackManager;
    private final Environment environment;

    @PostConstruct
    private void initialize() {
        log.debug("initialize");
        LogbackDefine.setFileNamePrefix(logRuleProperties.getFiles().getAbsolutePath());
        initLogLevel();
    }

    private void initLogLevel() {
        log.info("1Q Logback Configuration!!, Root LogLevel = [{}]", logRuleProperties.getLevel().getRoot());
        switch (logRuleProperties.getLevel().getRoot()) {
            case "INFO", "WARN", "DEBUG" -> {
                this.logbackManager.setLogLevel(Level.toLevel(logRuleProperties.getLevel().getRoot()));
                logRuleProperties.getLevel().getCls().forEach(x -> this.logbackManager.setLogLevel(LoggerFactory.getLogger(x), Level.toLevel(logRuleProperties.getLevel().getRoot()))); }
            default -> {
                this.logbackManager.setLogLevel(Level.ERROR);
                logRuleProperties.getLevel().getCls().forEach(x -> this.logbackManager.setLogLevel(LoggerFactory.getLogger(x), Level.ERROR)); }
        }

    }



}
