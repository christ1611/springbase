package com.springbase.core.security.userdetails;

import com.springbase.core.common.define.EnumDefine.YesNoDvCd;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import com.springbase.core.jpa.dsl.AcomAuthPtcl;
import com.springbase.core.jpa.repository.DaoAcomAuthBase;
import com.springbase.core.jpa.repository.DaoAcomAuthPtcl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserSVC {
    private final DaoAcomAuthBase daoAcomAuthBase;
    private final DaoAcomAuthPtcl daoAcomAuthPtcl;
    /**
     *
     * @param userId the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     * 정보 조회
     */
    @Override
    public Auth getAuth(String userId) {
        AcomAuthBase acomAuthBase = daoAcomAuthBase.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));

        AcomAuthPtcl acomAuthPtcl = daoAcomAuthPtcl.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        if (StringUtils.equals(acomAuthPtcl.getLockYn(), YesNoDvCd.YES.isValue()))
            throw new DisabledException("LOCKED ACCOUNT");
        return Auth.toAuth(acomAuthBase);
    }
}
