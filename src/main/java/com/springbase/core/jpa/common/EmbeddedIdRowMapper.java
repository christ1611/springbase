package com.springbase.core.jpa.common;

import jakarta.persistence.EmbeddedId;
import oracle.sql.TIMESTAMP;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EmbeddedIdRowMapper<T> implements RowMapper<T> {

    private final Class<T> type;

    public EmbeddedIdRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            T entity = type.getDeclaredConstructor().newInstance();

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);

                if (field.isAnnotationPresent(EmbeddedId.class)) {
                    Object embeddedId = field.getType().getDeclaredConstructor().newInstance();

                    for (Field embField : field.getType().getDeclaredFields()) {
                        embField.setAccessible(true);
                        String camelStr=toSnakeUpper(embField.getName());
                        try {
                            Class<?> fieldType = embField.getType();
                            Object value = rs.getObject(camelStr);
                            if (fieldType.equals(Long.class) && value instanceof Number) {
                                embField.set(embeddedId, ((Number) value).longValue());
                            } else if (fieldType.equals(Integer.class) && value instanceof Number) {
                                embField.set(embeddedId, ((Number) value).intValue());
                            } else {
                                embField.set(embeddedId, value);
                            }
                        } catch (SQLException e) {
                            // ignore missing columns
                        }
                    }
                    field.set(entity, embeddedId);

                } else {
                    try {
                        String camelStr=toSnakeUpper(field.getName());
                        Class<?> fieldType = field.getType();
                        Object value = rs.getObject(camelStr);
                        if (fieldType.equals(Long.class) && value instanceof Number) {
                            field.set(entity, ((Number) value).longValue());
                        } else if (fieldType.equals(Integer.class) && value instanceof Number) {
                            field.set(entity, ((Number) value).intValue());
                        } else {
                            field.set(entity, value);
                        }
                    } catch (SQLException e) {
                        // ignore missing columns
                    }
                }
            }
            for (Field field : type.getSuperclass().getDeclaredFields()) {
                String camelStr=toSnakeUpper(field.getName());
                Class<?> fieldType = field.getType();
                Object value = rs.getObject(camelStr);
                if (fieldType.equals(Long.class) && value instanceof Number) {
                    field.set(entity, ((Number) value).longValue());
                } else if (fieldType.equals(Integer.class) && value instanceof Number) {
                    field.set(entity, ((Number) value).intValue());
                }else if (fieldType.equals(LocalDateTime.class) && value instanceof TIMESTAMP ts) {
                        field.set(entity, ts.toLocalDateTime());
                } else {
                    field.set(entity, value);
                }
            }


            return entity;
        } catch (Exception e) {
            throw new SQLException("Failed to map row to " + type.getSimpleName(), e);
        }
    }

    public static String toSnakeUpper(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
                result.append(c);
            } else {
                result.append(Character.toUpperCase(c));
            }
        }
        return result.toString();
    }
}
