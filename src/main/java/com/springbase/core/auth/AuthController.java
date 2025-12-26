package com.springbase.core.auth;

import com.springbase.core.auth.model.AuthInput;
import com.springbase.core.auth.model.AuthOutput;
import com.springbase.core.auth.model.AuthRegistrationInput;
import com.springbase.core.auth.model.LoginInput;
import com.springbase.core.auth.module.AuthUtil;
import com.springbase.core.common.define.AuthDefine;
import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.common.model.SysInfoBuilder;
import com.springbase.core.component.OneQCTX;
import com.springbase.core.exception.CoreException;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import com.springbase.core.jpa.dsl.AcomAuthPtcl;
import com.springbase.core.jpa.repository.ModelJPA;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@Slf4j
@Controller
@RequestMapping("/api/core")
@RequiredArgsConstructor
public class AuthController {
    private final ModelJPA modelJPA;
    private final AuthUtil authUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Operation(summary = "login")
    @Tag(name = "1. Login", description = "Login and token management API")
    @PostMapping("/login")
    public AuthOutput login(@RequestBody LoginInput input,
                      @Parameter(name="sysInfo", description = "sysInfo", in = ParameterIn.DEFAULT)SysInfoBuilder sysInfo) throws Exception {
        log.debug("login Controller");
        AcomAuthPtcl userPtcl = modelJPA.findAcomAuthPtcl(input.getUserId());

        return AuthOutput.builder().rsltSts(AuthDefine.AuthStCd.PASSWORD_CORRECT.value).rstPwdYn(userPtcl.getResetPwdYn()).build();

    }

    @Operation(summary = "register user")
    @Tag(name = "2. User Controller", description = "User management API")
    @PostMapping("/registUser")
    public AuthOutput registUser(@RequestBody @Valid AuthRegistrationInput input,
                                 @Parameter(name="sysInfo", description = "sysInfo", in = ParameterIn.DEFAULT)SysInfoBuilder sysInfo) throws Exception {
        log.debug("[START] Reg");

//        checkInput(input);
        if (!StringUtils.equals(input.getPassword(),input.getReEnterPassword()))
            throw new CoreException(CoreErrCode.PWD_NOT_MATCH);
        if (StringUtils.isBlank(input.getUserId()))
            throw new CoreException(CoreErrCode.USER_IS_NULL);
        if (StringUtils.isBlank(input.getUserName()))
            throw new CoreException(CoreErrCode.USERNAME_IS_NULL);



        AuthOutput authOutput = authUtil.registUser(input);


        log.debug("[END] Register user");

        return authOutput;
    }


    @Operation(summary = "change password")
    @Tag(name = "2. User Controller", description = "User management API")
    @PostMapping("/chgPass")
    public AuthOutput changePassword(@RequestBody @Valid AuthInput input) throws Exception {
        log.debug("[START] changePassword");

//        checkInput(input);
        if (!StringUtils.equals(input.getNewPassword(),input.getReEnterNewPassword()))
            throw new CoreException(CoreErrCode.PWD_NOT_MATCH);
        if (StringUtils.isBlank(input.getUserId())) {
            throw new CoreException(CoreErrCode.USER_IS_NULL);
        }

        AcomAuthBase user = modelJPA.findAcomAuthBaseById(input.getUserId());

        if (user == null) {
            throw new CoreException(CoreErrCode.USER_NOT_FOUND);
        }

        AcomAuthPtcl userPtcl = modelJPA.findAcomAuthPtcl(input.getUserId());
        if(StringUtils.equals(userPtcl.getResetPwdYn(),"N"))//신규자 아님
        {
            // oldPassword 체크
            boolean matcheResult = bCryptPasswordEncoder.matches(input.getOldPassword(), user.getPassword());
            if(!matcheResult) {
                throw new CoreException(CoreErrCode.OLD_NEW_PWN_SAME);
            }
        }
        AuthOutput authOutput = authUtil.changePassword(input);
        userPtcl.setResetPwdYn("N");
        modelJPA.saveAcomAuthPtcl(userPtcl);


        log.debug("[END] changePassword");

        return authOutput;
    }



    @Operation(summary = "reset password")
    @Tag(name = "2. User Controller", description = "User management API")
    @PostMapping("/resetPass")
    public AuthOutput resetPassword(@RequestBody @Valid AuthInput input) throws Exception {
        log.debug("[START] resetPassword");

// TODO: precondition to check user

        AcomAuthBase user = modelJPA.findAcomAuthBaseById(input.getUserId());

        if (user == null) {
            throw new CoreException(CoreErrCode.USER_NOT_FOUND);
        }

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String alphanumeric = upper + lower + digits;
        int psswdLength = 6;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(psswdLength);
        for(int i = 0; i < psswdLength; i++) {
            int randomIndex = random.nextInt(alphanumeric.length());
            password.append(alphanumeric.charAt(randomIndex));
        }

        input.setNewPassword("1");


        AuthOutput authOutput = authUtil.resetPassword(input);


        log.debug("[END] changePassword");

        return authOutput;
    }


    @Operation(summary = "reset password")
    @Tag(name = "2. User Controller", description = "User management API")
    @PostMapping("/chngLngg")
    public AuthOutput changeLanguange(@RequestBody @Valid AuthInput input) throws Exception {
        log.debug("[START] chngLngg");

        AcomAuthPtcl user = modelJPA.findAcomAuthPtcl(input.getUserId());
        if (user == null) {
            throw new CoreException(CoreErrCode.USER_NOT_FOUND);
        }
        user.setLnggCd(input.getLnggCd());
        OneQCTX.getCtxSysInfo().setLnggCd(input.getLnggCd());
        modelJPA.saveAcomAuthPtcl(user);

        log.debug("[END] chngLngg");

        return AuthOutput.builder().lnggCd(input.getLnggCd()).build();
    }
}
