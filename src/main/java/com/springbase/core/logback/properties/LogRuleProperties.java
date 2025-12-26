package com.springbase.core.logback.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;


@Configuration
@ConfigurationProperties(prefix = "log")
@Slf4j @Getter @Setter
public class LogRuleProperties {

    private Files files = new Files();
    private Appender appender = new Appender();
    private Level level = new Level();
    private Kafka kafka = new Kafka();
    private String daemonIp;

    @Getter @Setter
    public class Files {
        private String absolutePath;
        private String webRootDir;
        private String rootUploadPath;
        private String maxSize;
        private int maxHistory;
    }

    @Getter @Setter
    public class Appender {
        private boolean console;
        private boolean file;
        private boolean fileSift;
        private boolean kafka;
        private boolean logstash;
        private boolean jsonLayout;
        private boolean filter;
    }

    @Getter @Setter
    public class Level {
        private String root;
        private List<String> cls;
        private Map<String, String> springbase;
    }

    @Getter @Setter
    public class Kafka {
        private String bootstrapServers;
        private String topic;
        private boolean jsonLayout;
    }

}
