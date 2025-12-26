package com.springbase.core.jpa.repository;


import com.springbase.core.jpa.common.IBaseRepository;
import com.springbase.core.jpa.dsl.CoreErrCd;
import com.springbase.core.jpa.dsl.QCoreErrCd;
import com.springbase.core.jpa.dsl.pk.CoreErrCdPk;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoCoreErrCd extends IBaseRepository<CoreErrCd, CoreErrCdPk> {
    public default CoreErrCd findActiveErrorCode(String errCd, String lnggDvCd, Long seqNo, String useYn) throws Exception {
        QCoreErrCd cec = QCoreErrCd.coreErrCd;
        return queryFactory
                .selectFrom(cec)
                .where(
                        cec.errCd.eq(errCd),
                        cec.lnggDvCd.eq(lnggDvCd),
                        cec.seqNo.eq(seqNo),
                        useYn != null ? cec.useYn.eq(useYn) : Expressions.TRUE.isTrue()
                )
                .fetchOne();
    }

    public default Long findMaxSeqNo(String errCd, String lnggDvCd) throws Exception {
        QCoreErrCd cec = QCoreErrCd.coreErrCd;
        return queryFactory
                .select(cec.seqNo.max())
                .from(cec)
                .where(
                        cec.errCd.eq(errCd),
                        cec.lnggDvCd.eq(lnggDvCd)
                )
                .fetchOne();
    }
    public default  CoreErrCd findErrorCode(String errCd, String lnggDvCd) throws Exception {
        QCoreErrCd cec = QCoreErrCd.coreErrCd;
        return queryFactory
                .selectFrom(cec)
                .where(
                        cec.errCd.eq(errCd),
                        cec.lnggDvCd.eq(lnggDvCd),
                        cec.seqNo.eq(0L)
                )
                .fetchOne();
    }
}