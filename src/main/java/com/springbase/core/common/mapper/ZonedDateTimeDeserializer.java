package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isBlank(p.getValueAsString())) {
            return null;
        }
        else {
            if (p.getValueAsString().length() == 21)
                return ZonedDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("uuuuMMddHHmmssSSS[VV]"));
            else if (p.getValueAsString().length() == 23) {
                String format = p.getValueAsString().substring(4,5);
                return ZonedDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("uuuu"+format+"MM"+format+"dd HHmmssSSS[VV]"));
            }
            else {
                return null;
            }
        }
    }
}