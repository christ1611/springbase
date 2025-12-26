package com.springbase.core.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;


@Slf4j
@Component
public class SpringBaseTransactionManager extends JpaTransactionManager {

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        log.debug("Transaction Begin [{}] [{}]", definition.getName(), definition.getPropagationBehavior());

        super.doBegin(transaction, definition);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        log.debug("Transaction doRollback Start [{}]", status);

        super.doRollback(status);

        log.debug("Transaction doRollback End");
    }
}
