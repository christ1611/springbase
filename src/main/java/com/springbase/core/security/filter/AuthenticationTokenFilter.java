package com.springbase.core.security.filter;

import com.springbase.core.component.OneQBeanUtils;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import com.springbase.core.jpa.repository.ModelJPA;
import com.springbase.core.security.exception.AuthInitializedException;
import com.springbase.core.security.token.JwtAuthenticationToken;
import com.springbase.core.security.token.RawAccessJwtToken;
import com.springbase.core.security.token.TokenFactory;
import com.springbase.core.security.userdetails.AuthDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.springbase.core.security.define.SecurityDefine.*;



@Slf4j
public class AuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationFailureHandler failureHandler;
    private final ModelJPA modelJPA = OneQBeanUtils.getBean(ModelJPA.class);

    @Autowired
    public AuthenticationTokenFilter(RequestMatcher matcher, AuthenticationFailureHandler failureHandler) {
        super(matcher);
        this.failureHandler = failureHandler;
    }
    /**
     *  @MethodName: attemptAuthentication
     *  @Author :
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest HttpServletResponse
     *  @Description token 인증 진행
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.debug("[START] TOKEN Authentication attemptAuthentication()");

        String accessToken = extractToken(request);
        Authentication authentication = extractAuth(accessToken);

        try {
            extractJwsClaims(authentication);
        } catch (Exception ex) {
            throw new AuthInitializedException(ex.getMessage(), ex);
        }
        return getAuthenticationManager().authenticate(authentication);
    }

    /**
     *  @MethodName: successfulAuthentication
     *  @Author :
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest HttpServletResponse FilterChain Authentication
     *  @Description : 성공
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        log.debug("[SUCCESS] TOKEN Authentication");
        TokenFactory tokenFactory = new TokenFactory();
        String accessToken = tokenFactory.createToken((AuthDetails) authResult.getPrincipal());

        response.setHeader(JWT_TOKEN_HEADER_PARAM, accessToken);
        response.setHeader("Access-Control-Expose-Headers", JWT_TOKEN_HEADER_PARAM + ",Content-Disposition");

        AcomAuthBase acomAuthBase = modelJPA.findAcomAuthBaseById(((AuthDetails) authResult.getPrincipal()).getUsername());
        acomAuthBase.setAccessToken(accessToken);
        acomAuthBase.setExpDtm(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(TOKEN_EXPIRATION_TIME));
        modelJPA.saveAcomAuthBase(acomAuthBase);

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    /**
     *  @MethodName: unsuccessfulAuthentication
     *  @Author :
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest HttpServletResponse AuthenticationException
     *  @Description : Failure
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    /**
     *  @MethodName: extractToken
     *  @Author :
     *  @Date : 2024-09-06
     *  @Param : HttpServletRequest
     *  @Description : token 추출
     */
    private String extractToken(HttpServletRequest request){

        String bearerToken = request.getHeader(JWT_TOKEN_HEADER_PARAM);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;

    }

    /**
     *  @MethodName: extractAuth
     *  @Author :
     *  @Date : 2024-09-06
     *  @Param : String accessToken
     *  @Description : Authentication 생성
     */
    protected Authentication extractAuth(String accessToken) {
        RawAccessJwtToken token = new RawAccessJwtToken(accessToken);
        return new JwtAuthenticationToken(token);
    }

    /**
     *  @MethodName: extractJwsClaims
     *  @Author :
     *  @Date : 2024-09-06
     *  @Param : Authentication
     *  @Description : JwsClaims 진행
     */
    protected Jws<Claims> extractJwsClaims(Authentication authentication) throws Exception {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        return rawAccessToken.parseClaims();
    }

}