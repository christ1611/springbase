package com.springbase.core.security.exception;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;



public class ErrorAwareRequestWrapper extends HttpServletResponseWrapper {

    private String Url;

    public ErrorAwareRequestWrapper(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
    }

    public ErrorAwareRequestWrapper(HttpServletResponse httpServletResponse, String url) {
        super(httpServletResponse);
        Url = "(" + url + ") Not Found!!!";
    }

}
