package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.springbase.core.common.define.CoreSystemDefine;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate>
{

    private final DateTimeFormatter formatter;

    public LocalDateSerializer()
    {
        super();
        formatter = CoreSystemDefine.DATE_FORMATTER;
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null) {
            gen.writeString("");
        }
        else {
            gen.writeString(value.format(formatter));
        }
    }
}