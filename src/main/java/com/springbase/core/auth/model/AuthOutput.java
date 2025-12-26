package com.springbase.core.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter @Setter @Builder
public class AuthOutput {

    private String rsltSts;
    private String lnggCd;
    private String newResetPwd;
    private String rstPwdYn; //사용자 Reset Passowrd 필요
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}