package com.springbase.core.jpa.repository;

import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.common.model.SysInfo;
import com.springbase.core.exception.CoreException;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import com.springbase.core.jpa.dsl.AcomAuthPtcl;
import com.springbase.core.jpa.dsl.CoreErrCd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** 
 * packageName : com.oneqon.common.jpa.repository 
 * fileName : ModelJPA 
 * author :
 * date : 2024-09-11 
 * description : Filter에서 필요한 query를 @Component화
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelJPA {
    private final DaoAcomAuthBase daoAcomAuthBase;
    private final DaoAcomAuthPtcl daoAcomAuthPtcl;

    private final DaoCoreErrCd    daoCoreErrCd;                              /* 일자포맷 */
    public AcomAuthBase findAcomAuthBaseById(String id) {
        return daoAcomAuthBase.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id));
    }

    public AcomAuthPtcl findAcomAuthPtcl(String id) {
        return daoAcomAuthPtcl.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id));
    }

    public AcomAuthBase saveAcomAuthBase(AcomAuthBase acomAuthBase) {
        return daoAcomAuthBase.save(acomAuthBase);
    }

    public AcomAuthPtcl saveAcomAuthPtcl(AcomAuthPtcl acomAuthPtcl) {
        return daoAcomAuthPtcl.save(acomAuthPtcl);
    }


    public CoreErrCd findCoreErrorCd(String errCd){
        try {
            return daoCoreErrCd.findErrorCode(errCd, SysInfo.getCtxLnggCd());
        } catch (Exception e) {
            throw new CoreException(CoreErrCode.ERROR_CAST_ENTITY); // or handle gracefully
        }

    }



}
