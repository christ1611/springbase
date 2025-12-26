package com.springbase.core.errMsg.model;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class ErrMsgOutput {

    private String errCd;
    private String errMsg;
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}