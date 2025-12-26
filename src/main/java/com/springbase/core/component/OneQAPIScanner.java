package com.springbase.core.component;

import com.springbase.core.jpa.dsl.AcomSvcInfo;
import com.springbase.core.jpa.repository.DaoAcomSvcInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class OneQAPIScanner  implements ApplicationRunner {
    @Autowired
    private Environment env;

    @Autowired
    private DaoAcomSvcInfo daoAcomSvcInfo;

    private final RequestMappingHandlerMapping handlerMapping;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scanControllers();
    }

    public OneQAPIScanner(
            @Qualifier("requestMappingHandlerMapping")
            RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }
    private void scanControllers() throws Exception {
        if (env.getProperty("spring.profiles.active").equals("local")) return;
        Map<RequestMappingInfo, HandlerMethod> mappings =
                handlerMapping.getHandlerMethods();
        List<String> registeredApi = daoAcomSvcInfo.findAppAcomSvcInfo(env.getProperty("biz.appName"), LocalDate.now());
        mappings.forEach((mappingInfo, handlerMethod) -> {

            Class<?> controllerClass = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            if (!controllerClass.getPackageName().startsWith("com.springbase")) {
                return;
            }
            if (controllerClass.getPackageName().startsWith("com.springbase.coreExt") || controllerClass.getPackageName().startsWith("com.springbase.core")) { // no need for API in extension
                return;
            }


            RequestMapping classMapping =
                    controllerClass.getAnnotation(RequestMapping.class);

            OneQServicePolicy logClassPolicy =
                    method.getAnnotation(OneQServicePolicy.class);

            String logFileYn = (logClassPolicy != null && !logClassPolicy.logEnabled())? "N":"Y";
            String classPath = (classMapping != null && classMapping.value().length > 0)
                    ? classMapping.value()[0]
                    : "";

            // Method-level paths (PostMapping resolved here)
            Set<String> paths = getPaths(mappingInfo);

            // HTTP methods (POST in your example)
            Set<RequestMethod> httpMethods =
                    mappingInfo.getMethodsCondition().getMethods();

            for (String path : paths) {
                try {
                    if (registeredApi.contains(method.getName()))
                    {
                        if (logClassPolicy != null)
                        {
                            log.info("Update Log Policy!!!");
                            AcomSvcInfo acomSvcInfo = daoAcomSvcInfo.findAcomSvcInfo(env.getProperty("biz.appName"), method.getName(), LocalDate.now());

                            acomSvcInfo.setLogFileYn(logFileYn);
                            acomSvcInfo.setSysUpdDtm(LocalDateTime.now());
                            daoAcomSvcInfo.update(acomSvcInfo);
                        }
                        registeredApi.remove(method.getName());
                    }
                    else
                    {
                        AcomSvcInfo oldAcomSvcInfo = daoAcomSvcInfo.findAcomSvcInfo(env.getProperty("biz.appName"), method.getName(), LocalDate.now());
                        if (oldAcomSvcInfo != null )
                        {
                            if ( oldAcomSvcInfo.getUseYn().equals("N"))
                            {
                                oldAcomSvcInfo.setUseYn("Y");
                                oldAcomSvcInfo.setSysUpdDtm(LocalDateTime.now());
                                daoAcomSvcInfo.update(oldAcomSvcInfo);
                            }

                        }
                        else
                        {
                            AcomSvcInfo acomSvcInfo = new AcomSvcInfo();
                            acomSvcInfo.setAppNm(env.getProperty("biz.appName"));
                            acomSvcInfo.setSvcCd(method.getName());
                            acomSvcInfo.setUseYn("Y");
                            acomSvcInfo.setSvcNm(method.getName());
                            acomSvcInfo.setLogFileYn(logFileYn);
                            acomSvcInfo.setUri(path);
                            acomSvcInfo.setApclStrDt(LocalDate.now().minusDays(1L));
                            acomSvcInfo.setApclEndDt(LocalDate.of(9999,12,31));
                            acomSvcInfo.setSysUpdDtm(LocalDateTime.now());
                            acomSvcInfo.setSysRegDtm(LocalDateTime.now());
                            acomSvcInfo.setRegEmpNo("COREUSR");
                            acomSvcInfo.setUpdEmpNo("COREUSR");
                            daoAcomSvcInfo.insert(acomSvcInfo);
                        }

                    }

                } catch (Exception e) {
                    log.info("NOT FOUND!!!");
                }
            }
        });
        for (String api: registeredApi)
        {
            AcomSvcInfo acomSvcInfo = daoAcomSvcInfo.findAcomSvcInfo(env.getProperty("biz.appName"), api, LocalDate.now());
            acomSvcInfo.setUseYn("N");
            acomSvcInfo.setSysUpdDtm(LocalDateTime.now());
            daoAcomSvcInfo.update(acomSvcInfo);
        }
    }
    private Set<String> getPaths(RequestMappingInfo mappingInfo) {
        // Spring Boot 2.x
        if (mappingInfo.getPatternsCondition() != null) {
            return mappingInfo.getPatternsCondition().getPatterns();
        }

        // Spring Boot 3.x (PathPatternParser)
        if (mappingInfo.getPathPatternsCondition() != null) {
            return mappingInfo.getPathPatternsCondition().getPatternValues();
        }

        return Set.of();
    }
}
