package com.springbase.core.auth.model;

import com.springbase.core.jpa.dsl.AcomAuthBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.springbase.core.common.define.AuthDefine.Role;
import com.springbase.core.common.define.AuthDefine.LedgStCd;

@Getter
@Setter
@NoArgsConstructor
public class AuthInput {

    private String      userId              ; //userId
    private String      password            ; //password
    private String      opDvCd              ; //Disposition '1'-Login '0'-Logout, '2'-담당 책임자 정보, '3'- password Check, 'A'-Excel log
    private String      rejectLogin = "N"   ; //Cancellation of existing users 'N'-Logon attempt 'Y'-Request to release
    private String      passwdLock          ; //Password Lock Request '0'-No Lock '1'-Lock
    private String      newPassword         ;
    private String      oldPassword         ;
    private String      reEnterNewPassword     ;
    private Role   role;
    private String thId;    //My Ip Address ;
    private String userName;
    private String lnggCd;
    private LedgStCd ledgStCd;


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
