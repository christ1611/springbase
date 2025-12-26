package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {
    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeString("");
        } else {
            try {
                gen.writeString(value.withZoneSameInstant(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
                return;
            } catch (Exception e) {
                //e.printStackTrace();
            }

            gen.writeString(value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        }
    }
}