package com.springbase.core.util;

import com.springbase.core.common.define.CoreSystemDefine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ObjectUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getMapValue(Map<String, Object> map, String key, Class<T> clazz) {
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val != null) {
                    if (clazz.isAssignableFrom(val.getClass())) {
                        return clazz.cast(val);
                    }
                    else
                    {
                        if (val instanceof String) {
                            if (BigDecimal.class.isAssignableFrom(clazz))
                                return clazz.cast(new BigDecimal(val.toString()));
                            else if (Long.class.isAssignableFrom(clazz))
                                return clazz.cast(Long.parseLong(val.toString()));
                            else if (Integer.class.isAssignableFrom(clazz))
                                return clazz.cast(Integer.parseInt(val.toString()));
                            else if (LocalDate.class.isAssignableFrom(clazz))
                                return (T) LocalDate.parse(map.get(key).toString().replace("\\", "").replace("-", ""), CoreSystemDefine.DATE_FORMATTER);
                            else
                                return clazz.cast(val);
                        }
                        else if (val instanceof LocalDate ld) {
                            if(String.class.isAssignableFrom(clazz))
                                return (T) DateUtil.cvtLocalDateToString(ld);
                        }
                    }
                }
                else
                {
                    return null;
                }
            } catch (Exception e) {
                if (BigDecimal.class.isAssignableFrom(clazz))
                    return clazz.cast("0");
                else if (Long.class.isAssignableFrom(clazz))
                    return clazz.cast("0");
                else if (Integer.class.isAssignableFrom(clazz))
                    return clazz.cast("0");
                else
                    return null;
            }
        } else {
            if (BigDecimal.class.isAssignableFrom(clazz))
                return clazz.cast("0");
            else if (Long.class.isAssignableFrom(clazz))
                return clazz.cast("0");
            else if (Integer.class.isAssignableFrom(clazz))
                return clazz.cast("0");
            else
                return null;
        }
        return null;
    }


}
