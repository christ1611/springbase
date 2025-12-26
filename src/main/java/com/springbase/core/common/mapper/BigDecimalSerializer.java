package com.springbase.core.common.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Slf4j
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    private final DecimalFormat df;

    public BigDecimalSerializer() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');

        df = new DecimalFormat("##0.######", decimalFormatSymbols);
    }

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null) {
            gen.writeString ("0.00");
        }
        else {
            gen.writeString(df.format(value));
        }
    }
}