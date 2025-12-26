package com.springbase.core.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthInitializedException extends AuthenticationException {
    public AuthInitializedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
