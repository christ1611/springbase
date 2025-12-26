package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if( StringUtils.isBlank(p.getValueAsString()) ) {
            return BigDecimal.ZERO;
        }
        else {
            return new BigDecimal(p.getValueAsString());
        }
    }
}