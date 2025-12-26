package com.springbase.core.security.token;

import com.springbase.core.security.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

import static com.springbase.core.security.define.SecurityDefine.TOKEN_SECRET_KEY;


@Slf4j
@Getter
public class RawAccessJwtToken {
    private String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    public Jws<Claims> parseClaims() {
        return this.parseClaims(TOKEN_SECRET_KEY);
    }

    private Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            log.info("JWT Token is expired. Please login again.", expiredEx);
            throw new JwtExpiredTokenException(signingKey, "JWT Token expired. Please login again.", expiredEx);
        }
    }
}
