package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

@Component
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeString("");
        } else {
            gen.writeString(value.format(new DateTimeFormatterBuilder().parseStrict()
                    .appendPattern("uuuu/MM/dd HH:mm:ss")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)));
        }
    }
}