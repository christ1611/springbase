package com.springbase.core.security.token;

import com.springbase.core.security.userdetails.AuthDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import static com.springbase.core.security.define.SecurityDefine.*;


@Component
public class TokenFactory {

    public String createToken(AuthDetails userContext) {

        LocalDateTime currentTime = LocalDateTime.now();
        Claims claims = Jwts.claims().setSubject(userContext.getUsername());

        return HEADER_PREFIX
                + Jwts.builder()
                .setClaims(claims)
                .claim("scopes", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()))
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(TOKEN_EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET_KEY)
                .compact();
    }

}