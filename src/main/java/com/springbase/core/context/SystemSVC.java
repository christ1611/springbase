package com.springbase.core.context;

import com.springbase.core.component.OneQCTX;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SystemSVC {
    protected abstract void preExecute(OneQCTX CTX) throws Exception;
    protected abstract void postExecute(OneQCTX CTX) throws Exception;

    public void prExecute() throws Exception {
        OneQCTX CTX = OneQCTX.getCTX();
        preSysExecute(CTX);
        preExecute(CTX);
    }

    public void poExecute() throws Exception {
        OneQCTX CTX = OneQCTX.getCTX();
        postExecute(CTX);
        postSysExecute(CTX);
    }

    /**
     * @param
     * @throws Exception
     * @Method Name : preSysExecute
     * @작성일 : 2017. 10. 31.
     * @작성자 : SonJooArm
     * @변경이력 :
     * @Method 설명 : 공통선처리
     */
    private void preSysExecute(OneQCTX CTX) throws Exception {

        //sysinfo에 환율정보를 선처리에서 올림, 추후 위치 변경 가능
//        ExhgRt ctxOnExhgRt  = new ExhgRt();
//        ExhgRt ctxOffExhgRt = new ExhgRt();
//
//        ctxOnExhgRt.setOnshOfShDvCd(EnumDefine.OnshOfShDvCd.ON_SHOW_TRSC);
//        ctxOnExhgRt.getCurrExhgRt(CTX.getSysInfo().getProcBascDt(), CTX.getSysInfo().getBaseCurCd());
//        CTX.loadExhgRt(ctxOnExhgRt);

        log.debug("systemSVC preSysExecute : SysInfo = [{}]", OneQCTX.getCtxSysInfo());
//        log.debug("systemSVC preSysExecute : EtcInfo = [{}]", OneQCTX.getCtxEtcInfo());
//        log.debug("systemSVC preSysExecute : OvrdInfo = [{}]", OneQCTX.getCtxOvrdInfo());
    }

    /**
     * @param
     * @throws Exception
     * @Method Name : postSysExecute
     * @작성일 : 2017. 10. 31.
     * @작성자 : SonJooArm
     * @변경이력 :
     * @Method 설명 : 공통후처리
     */
    private void postSysExecute(OneQCTX CTX) throws Exception {
        log.debug("systemSVC postSysExecute : SysInfo = [{}]", OneQCTX.getCtxSysInfo());
//        log.debug("systemSVC postSysExecute : EtcInfo = [{}]", OneQCTX.getCtxEtcInfo());
//        log.debug("systemSVC postSysExecute : OvrdInfo = [{}]", OneQCTX.getCtxOvrdInfo());
    }
}
