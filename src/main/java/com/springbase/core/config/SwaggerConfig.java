package com.springbase.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

@Slf4j
@Configuration
@OpenAPIDefinition(info =
@Info(title = "CoreSpring", version = "v0.0.1", description = "LOS",
        license = @License(name = "Apache 2.0", url = "http://springdoc.org")))
public class SwaggerConfig {

    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(DATE_FORMAT)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    public static final String DATETIME_FORMAT = "yyyyMMdd HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(DATETIME_FORMAT)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);
    public static final String JWT_TOKEN_HEADER_PARAM         = "X-Auth-Token";

    public SwaggerConfig() {
        Schema<LocalDate> shld = new Schema<>();
        shld.example(LocalDate.now().format(DATE_FORMATTER));
        Schema<LocalDateTime> shldt = new Schema<>();
        shldt.example(LocalDateTime.now().format(DATETIME_FORMATTER));

        Schema<ZonedDateTime> shzldt = new Schema<>();
        shzldt.example(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        SpringDocUtils.getConfig().replaceWithSchema(LocalDate.class, shld);
        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, shldt);
        SpringDocUtils.getConfig().replaceWithSchema(ZonedDateTime.class, shzldt);
    }

    @Bean
    public GroupedOpenApi core() {
        return GroupedOpenApi.builder()
                .group("01.login")
                .packagesToScan("com.springbase.core.auth")
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }




    @Bean
    public OpenApiCustomizer buildSecurityOpenApi() {
        // log.debug("GroupedOpenApi-buildSecurityOpenApi");
        SecurityScheme securityScheme = new SecurityScheme()
                .name(JWT_TOKEN_HEADER_PARAM)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("X-Auth-Token"))
                .getComponents().addSecuritySchemes("X-Auth-Token", securityScheme);
    }


}