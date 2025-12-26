package com.springbase.core.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.lang.reflect.Field;

public class SafeReflectionToStringBuilder extends ReflectionToStringBuilder {

    public SafeReflectionToStringBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    @Override
    protected boolean accept(Field field) {
        boolean accepted = super.accept(field);

        // java.time.* 패키지의 필드는 제외 (접근 불가 예외 방지)
        if (field.getType().getPackageName().startsWith("java.time")) {
            return false;
        }

        return accepted;
    }
}
