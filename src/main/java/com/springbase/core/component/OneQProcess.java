package com.springbase.core.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class OneQProcess {

    public static <T> T getBean(Class<T> clazz) {
        return Optional.ofNullable(OneQBeanUtils.getBean(clazz)).orElseGet(() -> null);
    }

    public static Object getBean(String beanName) {
        return Optional.ofNullable(OneQBeanUtils.getBean(beanName)).orElseGet(() -> null);
    }

    public Object callServiceNtx(String serviceClass, Object input, Map<String, Object> sysInfoMap) throws Exception {

        return OneQCTX.getCTX().callServiceNTX(serviceClass,input,  sysInfoMap);

    }

    public void svcPreProcessing() throws Exception {
        OneQCTX CTX = OneQCTX.getCTX();

        log.debug("OneQProcess Service PreProcess START!, Global ID = [{}]", CTX.getGlobId());

        log.debug("OneQProcess Service PreProcess End...");
    }

    public void svcPostProcessing() throws Exception {
    }

    /**
     *  @MethodName: preExecuteSvcLog
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param :
     *  @Description : 서비스 진입전 입력값 로그 저장
     */
    public void preExecuteSvcLog() throws Exception {
        OneQCTX CTX = OneQCTX.getCTX();
//        SysInfo ctxSysInfo = CTX.getSysInfo();

        if (StringUtils.isBlank(CTX.getGlobId())) { return; }
    }

    /**
     *  @MethodName: postExecuteSvcLog
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param :
     *  @Description : 서비스 종료후 결과 로그저장
     */
    public void postExecuteSvcLog() throws Exception {
        OneQCTX CTX = OneQCTX.getCTX();
//        SysInfo sysInfo = OneQCTX.getCtxSysInfo();

        if (StringUtils.isBlank(CTX.getGlobId())) { return; }
    }

    /**
     *  @MethodName: errExecuteSvcLog
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param :
     *  @Description : 서비스 종료후 에러 결과 로그저장
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void errExecuteSvcLog(String globalId, String empNo, Exception exSvc) throws Exception {
        if (StringUtils.isBlank(globalId)) { return; }

        String exception = "";
        String errCode = "";
        String errMsg = "";

        if (exSvc != null) {
            exception = ExceptionUtils.getStackTrace(exSvc);
            if (StringUtils.length(exception) > 4000)
                exception = StringUtils.left(exception, 4000);

            errMsg = exSvc.getMessage();
            if (StringUtils.length(errMsg) > 4000)
                errMsg = StringUtils.left(errMsg, 4000);
        }

        exception = getByteString(exception, 4000);
        errMsg = getByteString(errMsg, 1000);
//        String usrIpAdr = SysInfo.getCtxUserIpAddr();
    }


    public String getByteString(String str, int length) throws Exception {
        String EncodingLang = "UTF-8";
        byte[] bytes = str.getBytes("UTF-8");
        byte[] value = new byte[length];

        if (bytes.length < length) {
            return str;
        } else {
            for (int i = 0; i < length; i++) {
                value[i] = bytes[i];
            }
            return new String(value, EncodingLang).trim();
        }
    }
}
