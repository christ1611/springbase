package com.springbase.core.auth.model;

import com.springbase.core.common.define.AuthDefine.LedgStCd;
import com.springbase.core.common.define.AuthDefine.Role;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@NoArgsConstructor
public class AuthRegistrationInput {

    private String      userId              ; //userId
    private String      password            ; //password
    private String      reEnterPassword     ;
    private Role        role;
    private String      userName;
    private LedgStCd    ledgStCd;


    public AcomAuthBase toAuthInfo(String encryptPassword) {
        return AcomAuthBase.builder()
                .userId(this.userId)
                .password(encryptPassword)
                .userName(this.userName)
                .ledgStCd(this.ledgStCd.value)
                .role(this.role.name()).build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
