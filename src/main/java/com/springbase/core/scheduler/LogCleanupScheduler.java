package com.springbase.core.scheduler;

import com.springbase.core.logback.properties.LogRuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LogCleanupScheduler {

    private final LogRuleProperties logRuleProperties;

    public LogCleanupScheduler(LogRuleProperties logRuleProperties) {
        this.logRuleProperties = logRuleProperties;
    }

    @Scheduled(cron = "0 0 0 * * *") //매일 00.00
    public void deleteYesterdayLogs() {
        String yesterday = LocalDate.now()
                .minusDays(1)
                .format(DateTimeFormatter.BASIC_ISO_DATE);
        String filePath = logRuleProperties.getFiles().getAbsolutePath();
        Path yesterdayDir = Paths.get(filePath, yesterday);

        try {
            if (Files.exists(yesterdayDir)) {
                FileSystemUtils.deleteRecursively(yesterdayDir);
                log.info("Deleted yesterday log directory: {}", yesterdayDir);
            }
        } catch (Exception e) {
            log.warn("Failed to delete yesterday logs", e);
        }
    }
}