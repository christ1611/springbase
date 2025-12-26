package com.springbase.core.security.userdetails;

import com.springbase.core.jpa.dsl.AcomAuthBase;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Auth {
    private String userId;
    private String password;
    private List<GrantedAuthority> role;
    private Object entity;

    @Builder
    public Auth(String userId, String password, List<GrantedAuthority> role, Object entity) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.entity = entity;
    }

    /**
     *  @MethodName: toAuth
     *  @Author : handabin
     *  @Date : 2024-09-09
     *  @Param : AcomAuthBase
     *  @Description : auth 객체를 생성
     */
    public static Auth toAuth(AcomAuthBase authInfo) {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(authInfo.getRole()));
        return Auth.builder()
               .userId(authInfo.getUserId())
               .password(authInfo.getPassword())
               .role(roles)
               .entity(authInfo)
               .build();
    }
}
