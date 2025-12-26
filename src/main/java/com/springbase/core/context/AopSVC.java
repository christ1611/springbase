package com.springbase.core.context;


import com.springbase.core.component.OneQCTX;
import com.springbase.core.component.OneQProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Configuration
public class AopSVC {

    private final OneQProcess oneQProcess;

    @SuppressWarnings("unchecked")
    @Before("execution(public * com.springbase..*..*..*.execute(..))")
    public void aopCheck(JoinPoint joinPoint) throws Exception {
        OneQCTX CTX = OneQCTX.getCTX();
        log.info("[START] AopService aopCheck, className = [{}] methodName = [{}] inputClassName = [{}]", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), CTX.getInput().getClass());

        // 시스템선처리
        oneQProcess.svcPreProcessing();
//        SystemSVC svc = (SystemSVC) joinPoint.getTarget();
//        // 업무선처리
//        svc.prExecute();


        try {
            oneQProcess.preExecuteSvcLog();
        } catch (Exception e) {
            // 로깅은 에러무시.
        }
    }

    @AfterReturning(pointcut = "execution(public * com.springbase..*..*..*.execute(..))", returning = "retVal")
    public void afterReturning(JoinPoint joinPoint, Object retVal) throws Exception {
        log.debug("[START] AopSVC afterReturning()");
        OneQCTX CTX = OneQCTX.getCTX();
//        SystemSVC svc = (SystemSVC) joinPoint.getTarget();

        int transactionTime = (int) ((System.currentTimeMillis() - CTX.getTransactionStartTime()) / 10000.0f);
        log.debug("Check service execution time, transactionTime = [{}]", transactionTime);
//        if (transactionTime > SysInfo.getCtxToutDrtm()) {
//            throw new OneQSystemException(CoreErrCode.SERVICE_TIME_OUT, "[{}] Service TimeOut [{}][{}]", joinPoint.getTarget().getClass().getSimpleName()
//                    , CTX.getSvcInfo().getToutDrtm(), transactionTime);
//        }

        // 업무후처리
      //  svc.poExecute();
        // 시스템후처리
        oneQProcess.svcPostProcessing();
        try {
            oneQProcess.postExecuteSvcLog();
        } catch (Exception e) {
            log.error("Error during OneQProcess.postExcuteSvcLog()", e);
        }

    }


    /**
     *  @MethodName: throwingLogging
     *  @Author : handabin
     *  @Date : 2024-09-26
     *  @Param : JoinPoint Exception
     *  @Description : 데이터 처리 및 로깅 작업
     */
    @AfterThrowing(pointcut = "execution(public * com.springbase..*..*..*.execute(..))", throwing = "ex")
    public void throwingLogging(JoinPoint joinPoint, Exception ex) throws Exception {
        log.error("[START] AopSVC AfterThrowing()");
        log.error("AopSVC AfterThrowing Error Message = [{}]", ex.getMessage(), ex);

    }
}