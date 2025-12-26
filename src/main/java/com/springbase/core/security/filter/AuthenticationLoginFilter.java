package com.springbase.core.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbase.core.component.OneQCTX;
import com.springbase.core.context.CachedBodyHttpServletRequest;
import com.springbase.core.security.exception.AuthInitializedException;
import com.springbase.core.security.exception.AuthMethodNotSupportedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * packageName : com.oneqon.core.security.filter
 * fileName : AuthenticationLoginFilter
 * author : handabin
 * date : 2024-09-06
 * description : Login Authentication Filter
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-09-06     handabin      New
 */
@Slf4j
public class AuthenticationLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    public AuthenticationLoginFilter(OrRequestMatcher matchers, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, ObjectMapper objectMapper) {
        super(matchers);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = objectMapper;
    }

    /**
     *  @MethodName: attemptAuthentication
     *  @Author : handabin
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest HttpServletResponse1
     *  @Description : 인증 진행
     */
    @SuppressWarnings("unchecked")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("LOGIN Authentication attemptAuthentication()");
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        Map<String, Object> httpInput = objectMapper.readValue(cachedRequest.getReader(), LinkedHashMap.class);

        Map<String, Object> input = (Map<String, Object>) httpInput.get("input");
        String userId = (String) input.get("userId");
        String password = (String) input.get("password");

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(password)) {
            throw new AuthMethodNotSupportedException("User ID and Password required!!");
        }
        request.setAttribute("cachedRequest", cachedRequest);

        // AbstractUserDetailsAuthenticationProvider -> DaoAuthenticationProvider
        //initializeSystemInfo(userId, request, httpInput);
        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userId, password));
    }


    /**
     *  @MethodName: successfulAuthentication
     *  @Author : handabin
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest HttpServletResponse FilterChain Authentication
     *  @Description : 로그인 Success
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        try {
            // 요청을 CachedBodyHttpServletRequest로 래핑 하여 다음 필터에도 동일한 요청 본문을 사용 가능하게 정리
            CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request.getAttribute("cachedRequest");
            chain.doFilter(cachedRequest, response);
        } catch (IOException | ServletException e) {
            throw new AuthInitializedException(e.getMessage(), e);
        }
    }

    /**
     *  @MethodName: unsuccessfulAuthentication
     *  @Author : handabin
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest HttpServletResponse AuthenticationException
     *  @Description : 예외클래스와 함께 Failure
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 진행했던 Security Context 존재하는 내용 삭제
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
        MDC.clear();
    }

    private void initializeSystemInfo(String userId, HttpServletRequest request, Map<String, Object> httpInput) throws UnknownHostException {
        String[] url = request.getRequestURI().split("/");

        httpInput.put("procSvcCd", url[url.length - 1].replace(".", ""));

        try {
            OneQCTX.set(new OneQCTX());
            OneQCTX CTX = OneQCTX.getCTX();
            CTX.initializeCTX(userId, httpInput);
        }
        catch (Exception e) {
            throw new AuthInitializedException(e.getMessage(), e);
        }
    }
}