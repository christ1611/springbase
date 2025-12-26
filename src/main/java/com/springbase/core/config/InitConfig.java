package com.springbase.core.config;

import com.springbase.core.component.OneQBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class InitConfig {

    @Autowired
    @Bean
    public OneQBeanUtils oneQBeanUtils(ApplicationContext applicationContext) {
        return new OneQBeanUtils(applicationContext);
    }

}