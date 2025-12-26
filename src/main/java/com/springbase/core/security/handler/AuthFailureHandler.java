package com.springbase.core.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbase.core.context.CachedBodyHttpServletRequest;
import com.springbase.core.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error("Authentication FAIL.", e);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Connection", "close");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setException(e);
        if (e instanceof BadCredentialsException) {
            // replace message
            errResponse.setMessage("Invalid Username or Password"); //change to core error
        }
        else
        {
            errResponse.setMessage(e.getMessage());
        }


        CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request.getAttribute("cachedRequest");
        // 에러 발생 시, 이전에 넣어놨던 캐시데이터 생성
        if(cachedRequest != null){
//            Map<String, Object> httpInput = MapperUtil.mapper.readValue(cachedRequest.getReader(), LinkedHashMap.class);
//            Map<String, Object> input = (Map<String, Object>) httpInput.get("input");
//            String userId = (String) input.get("userId");;

            // 메모리에서 캐시 삭제
            cachedRequest.clearCachedBody();
        }

        ModelAndView mv = new ModelAndView("jsonView");
        mv.addObject("output", errResponse );
        mapper.writeValue(response.getWriter(), mv.getModelMap());
    }

}