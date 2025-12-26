package com.springbase.core.security.userdetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface UserSVC extends UserDetailsService {
    @Override
    default AuthDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Auth auth = getAuth(userId);
        return createAuthDetails(auth);
    }

    default AuthDetails createAuthDetails(Auth auth) {
        return new AuthDetails(auth.getUserId(), auth.getPassword(), true, auth);
    }

    Auth getAuth(String userId);
}