package com.springbase.core.config;

import com.springbase.core.transaction.SpringBaseTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.ValidationMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.springbase.core.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class JPAConfig {
    @Autowired
    private DataSource dataSource;

    /**
     *  @MethodName: entityManagerFactory
     *  @Author : nayoseph
     *  @Date : 2024-08-27
     *  @Param :
     *  @Description : EntityManager 인스턴스를 생성하는데 사용
     */
    @Bean @Primary
    public EntityManagerFactory entityManagerFactory() throws Exception {
        log.info("entityManagerFactory core start ......");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        // Hibernate.ddl-auto : false- none / true- create,update
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabase(Database.ORACLE);
        vendorAdapter.setPrepareConnection(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.OracleDialect");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan("com.springbase.core.jpa");
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();

        Properties pt = new Properties();
        pt.setProperty("hibernate.hbm2ddl.auto", "none");
        pt.setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        pt.setProperty("autoCommit","false");
        pt.setProperty("hibernate.format_sql", "true");

        factoryBean.setJpaProperties(pt);
        factoryBean.setValidationMode(ValidationMode.AUTO);

        return factoryBean.getObject();
    }


    @Bean(name = "transactionManager")
    public SpringBaseTransactionManager transactionManager() throws Exception {
        SpringBaseTransactionManager txManager = new SpringBaseTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }
}