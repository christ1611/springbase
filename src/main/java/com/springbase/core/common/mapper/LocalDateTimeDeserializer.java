package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isBlank(p.getValueAsString())) {
            return null;
        }
        else {
            if (p.getValueAsString().length() == 17) {
                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("uuuuMMdd HH:mm:ss"));
            }
            else if (p.getValueAsString().length() == 19) {
                String format = p.getValueAsString().substring(4,5);
                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("yyyy"+format+"MM"+format+"dd HH:mm:ss"));
            }
            else {
                return null;
            }
        }
    }
}