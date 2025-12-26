package com.springbase.core.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class RequestWrapperFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.debug("[START] RequestWrapperFilter");

//        //TODO: 화면쪽과 Body부 협의되면 풀기
//        filterChain.doFilter(servletRequest, servletResponse);

        if(!servletRequest.getMethod().equals(HttpMethod.POST.name())
            || StringUtils.startsWithIgnoreCase(servletRequest.getContentType(), "multipart/")
            || StringUtils.startsWithIgnoreCase(servletRequest.getRequestURI(), "/actuator")
            || StringUtils.startsWithIgnoreCase(servletRequest.getRequestURI(), "/h2-console")
            || StringUtils.startsWithIgnoreCase(servletRequest.getRequestURI(), "/swagger-ui/index.html")
            || StringUtils.startsWithIgnoreCase(servletRequest.getRequestURI(), "/api/file/download")
        ) {
            // Request Method가 POST가 아니거나 Content Type 이 Multipart 인 경우 다음 필터로 이동
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletRequest.setCharacterEncoding("UTF-8");
            RequestWrapper request = null;
            try {
                // RequestWrapper로 래핑
                request = (servletRequest instanceof RequestWrapper rw) ? rw : new RequestWrapper(servletRequest);
            } catch (Exception e) {
                log.error("Error running RequestWrapper!", e);
                throw new ServletException("RequestWrapper Error: " + e.getMessage(), e);
            }

            filterChain.doFilter(request, servletResponse);
        }
    }

}