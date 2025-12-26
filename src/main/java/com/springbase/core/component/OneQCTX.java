package com.springbase.core.component;

import com.springbase.core.common.model.*;
import com.springbase.core.context.ContextInitializer;
import com.springbase.core.exception.CoreException;
import com.springbase.core.exception.ReTranException;
import com.springbase.core.transaction.SpringBaseTransactionManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import static com.springbase.core.logback.LogbackDefine.MDC_GLOBAL_ID;



@Slf4j
@Getter
public class OneQCTX implements Serializable, Cloneable {

    public static String OneQInstanceId = getInstanceId();


    private static String getInstanceId()
    {
        try
        {
            if( OneQInstanceId != null) return OneQInstanceId;

            String id = "OneQ-"
                    + Inet4Address.getLocalHost().getHostName()
                    + "-"
                    + ProcessHandle.current().pid()
                    ;
            log.debug("InstanceId [{}]", id);
            return id;
        }
        catch(Exception ex)
        {
            log.error("error", ex);
            return UUID.randomUUID().toString();

        }
    }


    @Autowired(required = false)
    private List<ContextInitializer> contextInitializers;

    /* Service Transaction Start Time           */    private long transactionStartTime = 0;

    /* Service Transaction Sequence             */    private int svcCallSeq = 0;
    /* Service Transaction Sequence             */    private int maxSvcCallSeq = 1;
    public boolean svcLogging = true;


    /* 사용자의 입력값      httpRequest Map      */   private Map<String, Object> httpInput;
    /* 사용자의 화면 입력값 httpRequest Map      */   private Map<String, Object> input;
    /* 공통 시스템 정보                          */   private SysInfo sysInfo;
    /* 시스템 일자정보                           */   private DayInfo dayInfo;
    /* 사용자 정보                               */   private UserInfo userInfo;
    private String serviceUrl;
    private ArrayList<String> serviceList = new ArrayList<>();
    private OneQCTX parentCTX = null;



    @Override
    public OneQCTX clone() throws CloneNotSupportedException {
        return (OneQCTX) super.clone();
    }

    private static InheritableThreadLocal<OneQCTX> THREAD_LOCAL = new InheritableThreadLocal<>() {
        protected OneQCTX initialValue() {
            return new OneQCTX();
        }
    };

    public static void set(OneQCTX context) {
        THREAD_LOCAL.set(context);
    }

    public static void remove() {
        MDC.remove(MDC_GLOBAL_ID);
        THREAD_LOCAL.remove();

    }

    public OneQCTX() {
        this.transactionStartTime = System.currentTimeMillis();

        this.httpInput    = new LinkedHashMap<>();
        this.input        = new LinkedHashMap<>();
        this.sysInfo      = new SysInfo();
        this.userInfo = new UserInfo();
        this.dayInfo = new DayInfo();
    }

    public static OneQCTX getCTX() {
        return Optional.ofNullable(THREAD_LOCAL.get()).orElseGet(() -> null);
    }

    public String getGlobId() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(OneQCTX::getSysInfo)
                .map(SysInfo::getGlobId)
                .orElseGet(() -> null);
    }
    public String getGlobIdTrsc() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(OneQCTX::getSysInfo)
                .map(SysInfo::getGlobIdTrsc)
                .orElseGet(() -> null);
    }
    public static String getGlobalId() {
        if( getCTX() != null ){
            return getCTX().getGlobId();
        }
        return "";
    }

    public static String getGlobalIdTrsc() {
        if( getCTX() != null ){
            return getCTX().getGlobIdTrsc();
        }
        return "";
    }

    public static SysInfo getCtxSysInfo() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(OneQCTX::getSysInfo)
                .orElseGet(() -> null);
    }

    public static DayInfo getCtxBizDayInfo() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(OneQCTX::getDayInfo)
                .orElseGet(() -> null);
    }

    public static UserInfo getCtxCustInfo() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(OneQCTX::getUserInfo)
                .orElseGet(() -> null);
    }

    @SuppressWarnings("unchecked")
    public void initializeCTX(Map<String, Object> httpInput) throws Exception {

        Map<String, Object> httpCustInfo = new LinkedHashMap<>();


        if (httpInput.containsKey("custInfo")) {
            httpCustInfo = (Map<String, Object>) httpInput.get("custInfo");
            // TODO: 01/29/2024
            log.debug("CTX initialize SysInfo = [{}]", httpCustInfo);
        }

        if (httpInput.containsKey("input")) {
            this.input = (Map<String, Object>) httpInput.get("input");
            log.debug("CTX initialize input = [{}]", this.input);
        }

       // this.sysInfo.initInfo(httpSysInfo);
       // this.custInfo.initInfo();

    }


    @SuppressWarnings("unchecked")
    public void initializeCTX(String userId, Map<String, Object> httpInput) throws Exception {
        Map<String, Object> httpSysInfo = new LinkedHashMap<>();

        if (httpInput.containsKey("sysInfo")) {
            httpSysInfo = (Map<String, Object>) httpInput.get("sysInfo");
            httpSysInfo.put("userId",userId);
            // TODO: 01/29/2024
            log.debug("CTX initialize SysInfo = [{}]", httpSysInfo);
        }

        if (httpInput.containsKey("input")) {
            this.input = (Map<String, Object>) httpInput.get("input");
            log.debug("CTX initialize input = [{}]", this.input);
        }

        this.sysInfo.initInfo(httpSysInfo);

    }




    @SuppressWarnings("unchecked")
    public Object callService(String serviceClass, Object input, Map<String, Object> sysInfoMap) throws Exception {
        log.debug("## callService Start!!");
        log.debug("Call Service input = [{}]", input.toString());
        log.debug("Call Service sysInfoMap = [{}]", sysInfoMap);

        HttpServletRequest request = null;
        try {
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        }
        catch (IllegalStateException ex)
        {
            log.debug("No thread-bound request");
        }

        this.serviceList.add(serviceClass);
        this.maxSvcCallSeq++;

        /* backup CoreAcomContBase before call Service */
        Map<String, Object> inputOrg = this.input;


        sysInfoMap.put("serviceList", this.serviceList);
        sysInfoMap.put("maxSvcCallSeq", this.maxSvcCallSeq);
        sysInfoMap.put("trscSeqNo", 0L); //trscSeqNo 초기화
        this.sysInfo.initCallService(sysInfoMap);
//        this.brInfo.initInfo(this.sysInfo.getTrscBrNo());


        if( request != null ) request.setAttribute("OneQCTX", sysInfoMap);

//        this.input = ObjectUtil.copyObject(input, LinkedHashMap.class);

        Object svcObj = OneQBeanUtils.getBean(serviceClass);
        log.debug("Service Call {}", svcObj);

        Object rtnObj = null;
        for (Method m : svcObj.getClass().getMethods()) {
            if (StringUtils.equals(m.getName(), "execute")) {
                log.debug("Service Call {} {}", svcObj, m);
                try
                {
                    rtnObj = m.invoke(svcObj, input);
                    break;
                }
                catch (InvocationTargetException e)
                {
                    log.error ("Service Call {} {}", svcObj, m);
                    log.error ("####  e.getTargetException().getMessage() ####[{}]", e.getTargetException().getMessage());

                    Throwable tex = e.getTargetException();
                    if(tex instanceof NullPointerException ne)
                    {
                        throw ne;
                    }
                    else if(tex instanceof PersistenceException pe)
                    {
                        throw pe;
                    }
                    else if(tex instanceof SQLIntegrityConstraintViolationException sqle)
                    {
                        throw sqle;
                    }
                    else if(tex instanceof ReTranException re)
                    {
                        throw re;
                    }
                    else if(tex instanceof CoreException oqe)
                    {
                        throw oqe;
                    }

//                    throw new OneQLinkTranException(e.getTargetException());
                }
                catch (Exception e)
                {
                    log.error ("####  e.getTargetException().getMessage() ####[{}]", e.getMessage());
//                    throw new OneQLinkTranException(e);
                }
            }
        }

        /* restore preCoreAcomContBase after call service */
//        sysInfoOrg.put("maxSvcCallSeq", 1);
//        this.sysInfo.initCallService(sysInfoOrg);
//        if( request != null ) request.setAttribute("OneQCTX", sysInfoOrg);
        this.input = inputOrg;

        log.debug("#### input ####[{}]", this.input);
        return rtnObj;
    }

    public int maxSvcCallSeqMinus() {
        if (this.maxSvcCallSeq == 0) return 0;

        return --this.maxSvcCallSeq;
    }

    public void returnOvrdTransaction() throws Exception
    {

    }

    /**
     *  @MethodName: callAsyncVoidFunction
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param : afn
     *  @Description : 비동기로 리턴값이 없는 함수 실행 할경우 사용
     *                 별도 쓰레드로 실행되어 트랜잭션 이 분리됨.
     *                 기존 트랜잭션에 포함되지 않는 거래 이므로 주의해서 사용해야 합니다.
     *                 로그 적재등 주거래와 관계없는 경우에만 사용하기 바랍니다.
     */
    public void callAsyncVoidFunction(Runnable afn) throws Exception
    {
        OneQCTX nCtx = this.clone();
        CompletableFuture.runAsync(()->{

            SpringBaseTransactionManager txManager  = OneQBeanUtils.getBean(SpringBaseTransactionManager.class);
            TransactionTemplate txTemplate = new TransactionTemplate(txManager);
            txTemplate.setName("callAsyncVoidFunction");

            txTemplate.execute(status->{
                try {
                    OneQCTX.set(nCtx);
                    MDC.put(MDC_GLOBAL_ID, nCtx.getGlobId());
                    afn.run();
                }
                catch (Exception e)
                {
                    status.setRollbackOnly();
                    log.error("Async Call Method Error", e);
                }
                finally {
                    OneQCTX.remove();
                }
                return null;
            });
        });
    }

    /**
     *  @MethodName: callAsyncReturn
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param : cfn
     *  @Description : 비동기로 리턴값이 있는 서비스 실행 할경우 사용
     *                 별도 쓰레드로 실행되어 트랜잭션 이 분리됨.
     *                 기존 트랜잭션에 포함되지 않는 거래 이므로 주의해서 사용해야 합니다.
     *                 로그 적재등 주거래와 관계없는 경우에만 사용하기 바랍니다.
     *                 함수 실행후 결과값을 받기 위해 wait 하게되어 동기처리됨.
     */
    @SuppressWarnings("unchecked")
    public  <T> T callAsyncReturn(Callable<T> cfn, Class<T> clazz) throws Exception
    {
        OneQCTX nCtx = this.clone();
        CompletableFuture<Object> cfuture = CompletableFuture.supplyAsync(()->{
            SpringBaseTransactionManager txManager  = OneQBeanUtils.getBean(SpringBaseTransactionManager.class);
            TransactionTemplate        txTemplate = new TransactionTemplate(txManager);
            txTemplate.setName("callAsyncReturn - " +  clazz.getSimpleName() );

            return txTemplate.execute(status->{
                OneQCTX.set(nCtx);
                MDC.put(MDC_GLOBAL_ID, nCtx.getGlobId());
                try {
                    return cfn.call();
                }
                catch (Exception e)
                {
                    status.setRollbackOnly();
                    log.error("Async Call Method Error", e);
                    return e;
                }
                finally {
                    OneQCTX.remove();
                }
            });
        });

        Object rtnObj = cfuture.get();
        if(rtnObj instanceof Exception ex)
        {
            throw ex;
        }
        else
        {
            return (T) rtnObj;
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setDayInfo(DayInfo dayInfo) {
        this.dayInfo = dayInfo;
    }

    /**
     *  @MethodName: callServiceNTX
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param : serviceClass, input, sysInfoMap
     *  @Description : 비동기로 리턴값이 있는 서비스 실행 할경우 사용
     *                 별도 쓰레드로 실행되어 트랜잭션 이 분리됨.
     *                 기존 트랜잭션에 포함되지 않는 거래 이므로 주의해서 사용해야 합니다.
     *                 로그 적재등 주거래와 관계없는 경우에만 사용하기 바랍니다.
     *                 서비스 실행후 결과값을 받기 위해 wait 하게되어 동기처리됨.
     */
    public Object callServiceNTX(String serviceClass, Object input, Map<String, Object> sysInfoMap) throws Exception
    {
        OneQCTX nCtx = this.clone();

        CompletableFuture<Object> ft = CompletableFuture.supplyAsync(()->{
            SpringBaseTransactionManager txManager  = OneQBeanUtils.getBean(SpringBaseTransactionManager.class);
            TransactionTemplate        txTemplate = new TransactionTemplate(txManager);

            txTemplate.setName("AsyncNtxCall"+serviceClass );
            return txTemplate.execute(status->{

                OneQCTX.set(nCtx);
                MDC.put(MDC_GLOBAL_ID, nCtx.getGlobId());
                try {
                    return nCtx.callService(serviceClass, input, sysInfoMap);
                }
                catch (Exception e)
                {
                    status.setRollbackOnly();
                    log.error("Async Call Service Error", e);
                    return e;
                }
                finally {
                    OneQCTX.remove();
                }
            });

        });

        Object obj = ft.get();
        if(obj instanceof Exception e)
        {
            throw e;
        }

        return obj;
    }
}
