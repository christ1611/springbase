package com.springbase.core.config;

import com.springbase.core.component.OneQProcess;
import com.springbase.core.context.ControllerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Bean
    public OneQProcess oneQProcess(){
        return new OneQProcess();
    }

    @Bean
    public ControllerInterceptor controllerInterceptor() {
        log.debug("controllerInterceptor");
        return new ControllerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerInterceptor())
//                .addPathPatterns("/**/*.SVC")
        ;
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean // 세션에 지역 설정. default는 KOREAN = 'ko'
    public LocaleResolver messageLocaleResolver() { // 지역 설정
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREAN);
        return slr;
    }

    @Bean // 지역설정을 변경하는 인터셉터. 요청시 파라미터에 lang 정보를 지정하면 변경됨
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

}