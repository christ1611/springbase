package com.springbase.core.jpa.common;

import com.springbase.core.component.OneQBeanUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface IBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>
{

    JPAQueryFactory queryFactory = (JPAQueryFactory) OneQBeanUtils.getBean("coreQueryFactory");


    <S extends T> S insert(S entity) throws Exception;

    <S extends T> List<S> insert(Iterable<S> entities) throws Exception;

    <S extends T> S update(S entity) throws Exception;

    <S extends T> List<S> update(Iterable<S> entities) throws Exception;


}
