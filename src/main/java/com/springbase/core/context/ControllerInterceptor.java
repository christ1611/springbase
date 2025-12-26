package com.springbase.core.context;

import com.springbase.core.component.OneQCTX;
import com.springbase.core.jpa.dsl.AcomSvcInfo;
import com.springbase.core.jpa.dsl.pk.AcomSvcInfoPk;
import com.springbase.core.jpa.repository.DaoAcomSvcInfo;
import com.springbase.core.logback.LogbackDefine;
import com.springbase.core.logback.LogbackManagerImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class ControllerInterceptor implements AsyncHandlerInterceptor {

    @Autowired
    private Environment env;
    @Autowired
    private DaoAcomSvcInfo daoAcomSvcInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        String controllerId ="";
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();

            controllerId  = method.getName();

        }

        AcomSvcInfoPk acomSvcInfoPk = new AcomSvcInfoPk();
        acomSvcInfoPk.setAppNm(env.getProperty("biz.appName"));
        acomSvcInfoPk.setSvcCd(controllerId);
        AcomSvcInfo acomSvcInfo = daoAcomSvcInfo.findAcomSvcInfo(env.getProperty("biz.appName"),controllerId, LocalDate.now());

        if (acomSvcInfo != null && StringUtils.equals(acomSvcInfo.getLogFileYn(),"Y"))
        {
            MDC.put(LogbackDefine.MDC_SERVICE, controllerId);
            log.debug("### Logging enabled for [{}], service={}", uri, controllerId);
        } else {
            // No logging
            MDC.remove(LogbackDefine.MDC_SERVICE);
            log.debug("### Logging skipped for [{}]", uri);
        }
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String uri = request.getRequestURI();

        log.info("[START] ControllerInterceptor postHandle, uri = [{}] handler = [{}]", uri, handler);

//        if ((!uri.matches("^.+\\.SVC$") && !uri.matches("^.+\\.SVC\\/.+"))
//        ||  modelAndView == null) {
//            return;
//        }
        if (modelAndView ==null) return;
        OneQCTX CTX = OneQCTX.getCTX();
        modelAndView.setViewName("jsonView");

        String outputKey = "output";
        ModelMap modelMap = modelAndView.getModelMap();

        List<String> removeKeys = new ArrayList<>();
        for (String key : modelMap.keySet()) {
            Object val = modelMap.get(key);
            if (val == null) continue;
            if (StringUtils.endsWith(key, "sysInfoBuilder")) {
                removeKeys.add(key);
            }
        }

        for (String dKey : removeKeys) {
            log.trace("remove key [{}] ", dKey);
            modelMap.remove(dKey);
        }

        modelAndView.addObject("globalId", CTX.getGlobId());
        modelAndView.addObject("sysInfo", CTX.getSysInfo().getOutMap());
    }

    /**
     * @MethodName: afterCompletion
     * @Author : nayoseph
     * @Date : 2024-08-27
     * @Param :
     * @Description : View 작업까지 완료된 후 Client에 응답하기 바로 전에 호출.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        log.info("[START] ControllerInterceptor afterCompletion");

        String service = MDC.get(LogbackDefine.MDC_SERVICE);
        OneQCTX.remove();
        MDC.remove("userId");
        LogbackManagerImpl.afterMoveLogfile(service);
        //LogbackManagerImpl.afterMoveLogfileGz(gid);

        log.info("[END] ControllerInterceptor afterCompletion");
    }
}
