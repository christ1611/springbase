package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.springbase.core.common.define.CoreSystemDefine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateDeserializer extends JsonDeserializer<LocalDate>
{
    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

    private final DateTimeFormatter formatter ;

    public LocalDateDeserializer() {
        formatter = CoreSystemDefine.DATE_FORMATTER;
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if( StringUtils.isBlank(p.getValueAsString()) ) {
            return null;
        }
        else {
            try {
                return LocalDate.parse(p.getValueAsString(), formatter);
            }
            catch (Exception ex) {
                log.debug("value[{}] - formatter[{}]", p.getValueAsString(), formatter);
                log.error("Error ", ex);
                return null;
            }
        }
    }
}