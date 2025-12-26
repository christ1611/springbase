package com.springbase.core.logback.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Slf4j @Getter @Setter
public class MinioProperties {
    private boolean enable = false;
    private String bucketName;
    private String minioUrl;
    private String accessKey;
    private String secretKey;
}
