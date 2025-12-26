package com.springbase.core.common.model;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class BaseDto {
    public String toString() {
        String raw = ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        return StringEscapeUtils.unescapeJava(raw);
    }

}
