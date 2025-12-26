package com.springbase.core.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    @PersistenceContext
    private EntityManager coreEntityManager;

    @Bean
    public JPAQueryFactory coreQueryFactory() {
        return new JPAQueryFactory(coreEntityManager);
    }
}
