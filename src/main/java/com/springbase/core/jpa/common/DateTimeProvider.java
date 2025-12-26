package com.springbase.core.jpa.common;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;


import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;



@Component
public class DateTimeProvider implements org.springframework.data.auditing.DateTimeProvider
{
    @Override
    @Nonnull
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now());
    }
}