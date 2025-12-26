package com.springbase.core.security.handler;

import com.springbase.core.exception.ErrorResponse;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import com.springbase.core.jpa.repository.DaoAcomAuthBase;
import com.springbase.core.security.token.TokenFactory;
import com.springbase.core.security.userdetails.AuthDetails;
import com.springbase.core.security.util.IpAddressUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.springbase.core.security.define.SecurityDefine.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenFactory tokenFactory;
    private final DaoAcomAuthBase daoAcomAuthBase;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("LOGIN Authentication SUCCESS.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthDetails userContext = (AuthDetails) authentication.getPrincipal();

            String accessToken = tokenFactory.createToken(userContext);
            String forwardedIp = IpAddressUtils.getForwardedIp(request);
            String userIpaddress = IpAddressUtils.getUserIpaddress(request);

            response.setHeader(JWT_TOKEN_HEADER_PARAM, accessToken);
            response.setHeader(FORWARDED_FOR, forwardedIp);
            response.setHeader(REAL_IP, userIpaddress);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT,OPTIONS");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Expose-Headers", String.join(",", JWT_TOKEN_HEADER_PARAM, FORWARDED_FOR, REAL_IP));

            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            AcomAuthBase acomAuthBase = daoAcomAuthBase.findById(userContext.getUsername())
                            .orElseThrow(() -> new UsernameNotFoundException(userContext.getUsername()));
            acomAuthBase.setAccessToken(accessToken);
            acomAuthBase.setExpDtm(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(TOKEN_EXPIRATION_TIME));
            daoAcomAuthBase.update(acomAuthBase);
//            Map<String, Object> sysInfo = OneQCTX.getCtxSysInfo().getOutMap();
//            sysInfo.remove("globId");
//            resultMap.put("sysInfo", sysInfo);
//            CustInfo custInfo = new CustInfo();
//            custInfo.setUserId(userContext.getUsername());
//            resultMap.put("output", custInfo);
//            objectMapper.writeValue(response.getWriter(), resultMap);
            clearAuthenticationAttributes(request);
        } catch (Exception e) {
            // 정상 반환 객체 조립 시 예외 발생 했을 시
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Connection", "close");
            response.setCharacterEncoding("UTF-8");

            ErrorResponse errResponse = new ErrorResponse();
            errResponse.setErrorCode(401);
            errResponse.setTimeStamp(LocalDateTime.now());
            errResponse.setException(e);

            errResponse.setMessage(e.getMessage());

            ModelAndView mv = new ModelAndView("jsonView");
            mv.addObject("output", errResponse );
        }

    }

    /**
     *  @MethodName: clearAuthenticationAttributes
     *  @Author : handabin
     *  @Date : 2024-09-26
     *  @Param : HttpServletRequest
     *  @Description : 세션정리
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {return;}
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}


