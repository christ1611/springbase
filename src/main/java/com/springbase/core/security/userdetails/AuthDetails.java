package com.springbase.core.security.userdetails;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class AuthDetails extends User {
    private Auth auth;

    /**
     *
     * @param username                 사용자 계정
     * @param password                 사용자 패스워드
     * @param enabled                  사용자계정 사용여부
     * @param accountNonExpired        계정 만료 여부
     * @param credentialsNonExpired    credential 만료 여부
     * @param accountNonLocked         계정 LOCK 여부
     * @param authorities              호출자 권한
     */
    public AuthDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired
            , boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Object userVO) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.auth = (Auth) userVO;
    }

    public AuthDetails(String username, String password, boolean enabled, Object userVO) {
        this(username, password, enabled, true, true, true, ((Auth) userVO).getRole(), userVO);
    }


}
