package com.springbase.core.component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;


public class OneQBeanUtils implements ApplicationContextAware {

    private static OneQBeanUtils staticContext;
    private ApplicationContext applicationContext;

    public OneQBeanUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz)
    {
        return Optional.of(staticContext.applicationContext.getBean(clazz)).orElse(null);
    }

    public static Object getBean(String beanName) {
        return staticContext.applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void registerInstance() {
        staticContext = this;
    }
}
