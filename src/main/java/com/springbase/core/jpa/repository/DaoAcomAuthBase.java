package com.springbase.core.jpa.repository;


import com.springbase.core.jpa.common.IBaseRepository;
import com.springbase.core.jpa.dsl.AcomAuthBase;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoAcomAuthBase extends IBaseRepository<AcomAuthBase, String> {
}