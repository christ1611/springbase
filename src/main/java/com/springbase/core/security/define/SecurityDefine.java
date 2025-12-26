package com.springbase.core.security.define;

public class SecurityDefine {
    public static final String BATCH_TOKEN_HEADER_PARAM = "BATCH-Auth-Token";
    public static final String JWT_TOKEN_HEADER_PARAM = "X-Auth-Token";
    public static final String FORWARDED_FOR = "X-Forwarded-For";
    public static final String REAL_IP = "X-Real-IP";
    public static final String HEADER_PREFIX = "Bearer ";
    public static final String TOKEN_SECRET_KEY = "b25lcS5vbi5hcGkuZ2F0ZXdheS5qd3Quc2VjcmV0LmtleS5vbmVxLm9uLmFwaS5nYXRld2F5Lmp3dC5zZWNyZXQua2VfjVkeWASaZXEu"; //Jwt Token Sign key
    public static final Integer TOKEN_EXPIRATION_TIME = 120;
}

