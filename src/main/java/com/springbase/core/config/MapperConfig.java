package com.springbase.core.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springbase.core.common.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper objectMapper() {

        Jackson2ObjectMapperBuilder j2om = Jackson2ObjectMapperBuilder.json();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

        javaTimeModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());

        j2om.modules(javaTimeModule);

        ObjectMapper mapper = j2om.build()
                .configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), true)
                // 모르는 property에 대해 무시하고 넘어간다. DTO의 하위 호환성 보장에 필요하다.
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // ENUM 값이 존재하지 않으면 null로 설정한다. Enum 항목이 추가되어도 무시하고 넘어가게 할 때 필요하다.
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)//
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                // Enum 값을 String으로 처리
                .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        SerializerProvider sp = mapper.getSerializerProvider();
        sp.setNullValueSerializer(new NullSerializer());

        return mapper;
    }

    @Bean
    public MappingJackson2JsonView jsonView() {
        MappingJackson2JsonView ov = new MappingJackson2JsonView();
        ov.setContentType("application/json;charset=UTF-8");
        ov.setObjectMapper(objectMapper());

        return ov;
    }
}