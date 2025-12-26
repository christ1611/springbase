package com.springbase.core.security.provider;

import com.springbase.core.security.token.JwtAuthenticationToken;
import com.springbase.core.security.token.RawAccessJwtToken;
import com.springbase.core.security.userdetails.AuthDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class AuthProvider implements AuthenticationProvider {
    protected UserDetailsService userDetailsService;

    protected AuthProvider(UserDetailsService userDetailsService) {this.userDetailsService = userDetailsService;}

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("[START] Authentication");

        if (authentication == null) {
            throw new IllegalArgumentException("No authentication data provided.");
        }

        Jws<Claims> jwsClaims = ((RawAccessJwtToken) authentication.getCredentials()).parseClaims();
        AuthDetails authDetails = (AuthDetails) userDetailsService.loadUserByUsername(jwsClaims.getBody().getSubject());
        try {
            return createJwtAuthenticationToken(jwsClaims.getBody(), authDetails);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    protected List<GrantedAuthority> getGrantedAuthorityList(Claims claims) {
        List<?> scopes = claims.get("scopes", ArrayList.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());
        return authorities;
    }

    protected AbstractAuthenticationToken createJwtAuthenticationToken(Claims claims, AuthDetails tifsUserDetail) throws Exception {
        return new JwtAuthenticationToken(tifsUserDetail, getGrantedAuthorityList(claims));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}