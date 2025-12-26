package com.springbase.core.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * packageName : com.oneqon.core.transaction
 * fileName : OneQTransactionAttributeSource
 * author : nayoseph
 * date : 2024-10-02
 * description :
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-10-02     nayoseph      New
 */

@Slf4j
public class SpringBaseTransactionAttributeSource implements TransactionAttributeSource, Serializable {

    private final Integer DEFAULT_TIMEOUT;
    private final TransactionAttribute DEFAULT_TRANSACTION_ATTRIBUTE;

    public SpringBaseTransactionAttributeSource(Integer defaultTimeout) {
        if (defaultTimeout == null || defaultTimeout == 0)
        {
            DEFAULT_TIMEOUT = 600;
        }
        else
        {
            DEFAULT_TIMEOUT = defaultTimeout;
        }
        DEFAULT_TRANSACTION_ATTRIBUTE = getDefaultTransactionAttribute();
    }

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass)
    {
//        if ("execute".equals(method.getName()) && SystemSVC.class.isAssignableFrom(targetClass))
//        {
//            RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
//            transactionAttribute.setName("1QTransaction-" + targetClass.getSimpleName());
//            transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//            transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
//
//            Integer txTimeOutSec = SysInfo.getCtxToutDrtm().intValue();
//
//            if (txTimeOutSec == null || txTimeOutSec == 0)
//            {
//                txTimeOutSec = 600;
//            }
//
//            transactionAttribute.setTimeout(txTimeOutSec);
//
//            log.debug("Transaction Name [{}]    TimeOut [{}] [{}]", transactionAttribute.getName(), transactionAttribute.getTimeout(), txTimeOutSec);
//            return transactionAttribute;
//        }
//        else
//        {
//            return DEFAULT_TRANSACTION_ATTRIBUTE;
//        }
        return getDefaultTransactionAttribute();
    }

    private TransactionAttribute getDefaultTransactionAttribute()
    {
        if (DEFAULT_TRANSACTION_ATTRIBUTE == null)
        {
            RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
            transactionAttribute.setName("BaseTransaction");
            transactionAttribute.setPropagationBehavior(0);
            transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
            transactionAttribute.setTimeout(DEFAULT_TIMEOUT);
            return transactionAttribute;
        }
        else
        {
            return DEFAULT_TRANSACTION_ATTRIBUTE;
        }
    }
}
