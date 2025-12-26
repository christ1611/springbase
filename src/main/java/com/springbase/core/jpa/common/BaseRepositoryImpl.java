package com.springbase.core.jpa.common;

import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.exception.CoreException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements IBaseRepository<T, ID> {


    private final EntityManager entityManager;


    public BaseRepositoryImpl(
            JpaEntityInformation<T, ID> entityInformation,
            EntityManager entityManager) {

        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    public <S extends T> S insert(S entity) throws Exception {
        try {
            // insert 실행 전 flush 처리해서 insert 시 오류와 구분함
            this.entityManager.flush();
            this.entityManager.persist(entity);
            return entity;
        } catch (Exception var3) {
            Exception ex = var3;
            log.error("An error occured, Table name : {}", entity.getClass().getSimpleName(), ex);
            throw new CoreException(CoreErrCode.UNKNOWN_DBMS_ERROR, ex.getMessage());
        }
    }


    @Transactional
    public <S extends T> List<S> insert(Iterable<S> entities) throws Exception
    {
        List<S> result = new ArrayList<>();

        if (entities == null) {
            return result;
        }

        for (S entity : entities) {
            result.add(insert(entity));
        }

        return result;
    }


    @Transactional
    public <S extends T> S update(S entity) throws Exception {
        Object pk = this.entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
        T exists = this.entityManager.find(this.getDomainClass(), pk);
        if (exists == null) {
            throw new CoreException(CoreErrCode.PK_NOT_FOUND, String.valueOf(pk));
        } else {
            entity = this.entityManager.merge(entity);

            return entity;
        }
    }

    @Transactional
    public <S extends T> List<S> update(Iterable<S> entities) throws Exception
    {
        List<S> result = new ArrayList<>();

        if (entities == null) {
            return result;
        }

        for (S entity : entities) {
            result.add(update(entity));
        }

        return result;
    }




    @Transactional
    public <S extends T> S save(S entity) {
        entity = super.save(entity);
        log.debug("entity={}", entity);

        return entity;
    }

}
