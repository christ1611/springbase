package com.springbase.core.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfig {
    @PostConstruct
    public void init() {
        log.debug("SchedulingConfig loaded");
    }
}
