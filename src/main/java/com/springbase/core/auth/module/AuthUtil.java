package com.springbase.core.auth.module;

import com.springbase.core.auth.model.AuthInput;
import com.springbase.core.auth.model.AuthOutput;
import com.springbase.core.auth.model.AuthRegistrationInput;
import com.springbase.core.common.define.AuthDefine;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import com.springbase.core.common.define.AuthDefine.AuthStCd;
import com.springbase.core.jpa.dsl.AcomAuthPtcl;
import com.springbase.core.jpa.repository.DaoAcomAuthBase;
import com.springbase.core.jpa.repository.DaoAcomAuthPtcl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @packageName : com.oneqoncore.common.auth.module
 * @fileName : OneQOneQAuth
 * @author : slee
 * @date : 2023-03-08
 * @description : 유저 관련 정보(상세 정보/패스워드/상태값/권한)를 변경하는 모듈
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2023-03-08     slee      New
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final DaoAcomAuthBase daoAcomAuthBase;
    private final DaoAcomAuthPtcl daoAcomAuthPtcl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AcomAuthBase getUserInfo(String userId) throws Exception {
        return daoAcomAuthBase.findById(userId).orElse(null);
    }

    public AuthOutput registUser(AuthRegistrationInput input) throws Exception {
        log.debug("[START] 1Q Auth Customer Registration Module, INPUT = [{}]", input.toString());

        String encryptPassword = bCryptPasswordEncoder.encode(input.getPassword());
        input.setLedgStCd(AuthDefine.LedgStCd.ACTIVE);
        AcomAuthBase authInfo = input.toAuthInfo(encryptPassword);

        daoAcomAuthBase.insert(authInfo);
        AcomAuthPtcl acomAuthPtcl = new AcomAuthPtcl();
        acomAuthPtcl.setUserId(input.getUserId());
        acomAuthPtcl.setLnggCd("KR"); //TODO default parameter
        acomAuthPtcl.setFailCount(0L);
        acomAuthPtcl.setLockYn("N");
        acomAuthPtcl.setResetPwdYn("Y");
        daoAcomAuthPtcl.insert(acomAuthPtcl);
        return AuthOutput.builder().rsltSts(AuthStCd.PASSWORD_CORRECT.value).build();
    }

    public void updateUser(AuthInput input) throws Exception {
        log.debug("[START] 1Q Auth Customer Registration Module, INPUT = [{}]", input.toString());

        String encryptPassword = bCryptPasswordEncoder.encode(input.getPassword());
        AcomAuthBase authInfo = input.toAuthInfo(encryptPassword); // pk : userId

        daoAcomAuthBase.delete(authInfo);
        daoAcomAuthBase.insert(authInfo);
        log.debug("[END] 1Q Auth Customer Update Module");
    }

    public AuthOutput changePassword(AuthInput input) throws Exception {
        log.debug("[START] 1Q Auth Password Modify Module, INPUT = [{}]", input.toString());


        String encryptPassword = bCryptPasswordEncoder.encode(input.getNewPassword());

        AcomAuthBase authInfo = daoAcomAuthBase.findById(input.getUserId()).orElse(null);
        if(authInfo == null) {
            return AuthOutput.builder().rsltSts(AuthStCd.INVALID_PASSWORD.value).build();
        }

        authInfo.setPassword(encryptPassword);

        daoAcomAuthBase.update(authInfo);


        return AuthOutput.builder().rsltSts(AuthStCd.PASSWORD_CORRECT.value).build();
    }

    public AuthOutput resetPassword(AuthInput input) throws Exception {
        log.debug("[START] 1Q Auth Password Modify Module, INPUT = [{}]", input.toString());


        String encryptPassword = bCryptPasswordEncoder.encode(input.getNewPassword());

        AcomAuthBase authInfo = daoAcomAuthBase.findById(input.getUserId()).orElse(null);
        if(authInfo == null) {
            return AuthOutput.builder().rsltSts(AuthStCd.INVALID_PASSWORD.value).build();
        }

        authInfo.setPassword(encryptPassword);
        daoAcomAuthBase.update(authInfo);

        return AuthOutput.builder().rsltSts(AuthStCd.PASSWORD_CORRECT.value).newResetPwd(input.getNewPassword()).build();
    }

    public AuthOutput validPassword(AuthInput input) throws Exception {

        return AuthOutput.builder().rsltSts(AuthStCd.PASSWORD_CORRECT.value).build();
    }

    public String encryptPassword(String password) throws Exception {
        return bCryptPasswordEncoder.encode(password);
    }

    public Boolean matchPassword(String inputPassword, String dbPassword) throws Exception {

        return bCryptPasswordEncoder.matches(inputPassword, dbPassword);
    }




    public AuthOutput initPassword(AuthInput input) throws Exception {
        log.debug("[START] 1Q Auth Customer Init Password Module, INPUT = [{}]", input.toString());

        String encryptPassword = bCryptPasswordEncoder.encode(input.getPassword());
        AcomAuthBase authInfo = daoAcomAuthBase.findById(input.getUserId()).orElse(null);
        if(authInfo == null) {
            authInfo = input.toAuthInfo(encryptPassword);
            daoAcomAuthBase.insert(authInfo);
        }
        else {
            authInfo.setPassword(encryptPassword);
            daoAcomAuthBase.update(authInfo);
        }

        return AuthOutput.builder().rsltSts(AuthStCd.PASSWORD_CORRECT.value).build();
    }
}