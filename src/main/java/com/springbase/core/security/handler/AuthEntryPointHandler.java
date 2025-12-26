package com.springbase.core.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbase.core.component.OneQCTX;
import com.springbase.core.context.CachedBodyHttpServletRequest;
import com.springbase.core.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthEntryPointHandler implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

        // HttpServletResponse에 에러 그대로 보내는 대신, 조립하여 객체로 전송하도록 함
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Connection", "close");
        response.setCharacterEncoding("UTF-8");

        // ErrorResponse 조립
        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setErrorCode(500);
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setException(e);
        errResponse.setSubMessage(List.of(e.getMessage()));

        // ModelAndView 조립
        ModelAndView mv = new ModelAndView("jsonView");
        mv.addObject("output", errResponse);
        try {
            mv.addObject("GlobalId", OneQCTX.getGlobalId());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        mapper.writeValue(response.getWriter(), mv.getModelMap());

        // 에러 발생 시, 이전에 넣어놨던 캐시데이터 생성
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        if(cachedRequest != null){
            // 메모리에서 캐시 삭제
            cachedRequest.clearCachedBody();
        }
    }
}

