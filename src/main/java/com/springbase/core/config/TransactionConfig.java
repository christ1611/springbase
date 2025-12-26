package com.springbase.core.config;

import com.springbase.core.transaction.SpringBaseTransactionAttributeSource;
import com.springbase.core.transaction.SpringBaseTransactionInterceptor;
import com.springbase.core.transaction.SpringBaseTransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.TransactionInterceptor;



@Slf4j
@Aspect
@Configuration
public class TransactionConfig {

    @Autowired @Qualifier("transactionManager")
    private SpringBaseTransactionManager transactionManager;

    @Value("#{new Integer('${oneqoncore.transaction.defaultTimeoutSec:60}')}")
    private Integer DEFAULT_TIMEOUT;

    @Bean
    public TransactionInterceptor txAdvice() {
        log.debug("txAdvice START ......");
        SpringBaseTransactionAttributeSource source    = new SpringBaseTransactionAttributeSource(DEFAULT_TIMEOUT);
        SpringBaseTransactionInterceptor txInterceptor = new SpringBaseTransactionInterceptor();
        txInterceptor.setTransactionAttributeSource(source);
        txInterceptor.setTransactionAttributeSource(source);
        txInterceptor.setTransactionManager(transactionManager);
        return txInterceptor;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        log.info("txAdviceAdvisor START ......");

        AspectJExpressionPointcut requiredTx = new AspectJExpressionPointcut();
        requiredTx.setExpression("execution(public * com.springbase..*..*..*.execute(..))");

        return new DefaultPointcutAdvisor(requiredTx, txAdvice());
    }
}
