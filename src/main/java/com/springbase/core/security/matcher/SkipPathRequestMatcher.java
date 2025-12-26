package com.springbase.core.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.stream.Collectors;


public class SkipPathRequestMatcher implements RequestMatcher {

    private final HttpMethod httpMethod;
    private OrRequestMatcher orRequestMatcher;
    private RequestMatcher requestMatcher;

    public SkipPathRequestMatcher(HttpMethod httpMethod, List<String> pathsToSkip, List<String> processingPath) {
        this.httpMethod = httpMethod;
        List<RequestMatcher> skipPathList = pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        List<RequestMatcher> pathList = processingPath.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        orRequestMatcher = new OrRequestMatcher(skipPathList);
        requestMatcher = new OrRequestMatcher(pathList);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (this.httpMethod != null && StringUtils.isNoneBlank(request.getMethod())
                && this.httpMethod != HttpMethod.valueOf(request.getMethod())) {
            return false;
        }

        return orRequestMatcher.matches(request) ? false : requestMatcher.matches(request);
    }
}
