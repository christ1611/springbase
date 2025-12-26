package com.springbase.core.util;

import com.springbase.core.common.define.CoreSystemDefine;

import java.time.LocalDate;

public class DateUtil {

    public static String cvtLocalDateToString(LocalDate dt)  throws Exception {
        String lds = "";
        if(dt == null ) return lds;

        lds = dt.format(CoreSystemDefine.DATE_FORMATTER );

        return lds;
    }
}
