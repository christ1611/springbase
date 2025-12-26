package com.springbase.core.common.define;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public class CoreSystemDefine {

    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(DATE_FORMAT)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

}
