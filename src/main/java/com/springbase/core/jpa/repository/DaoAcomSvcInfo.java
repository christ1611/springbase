package com.springbase.core.jpa.repository;


import com.springbase.core.jpa.common.IBaseRepository;
import com.springbase.core.jpa.dsl.AcomSvcInfo;
import com.springbase.core.jpa.dsl.QAcomSvcInfo;
import com.springbase.core.jpa.dsl.pk.AcomSvcInfoPk;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DaoAcomSvcInfo extends IBaseRepository<AcomSvcInfo, AcomSvcInfoPk> {
  public default AcomSvcInfo findAcomSvcInfo(String appNm, String svcCd, LocalDate applDt) throws Exception
  {
    QAcomSvcInfo acomSvcInfo = new QAcomSvcInfo("acomSvcInfo");
    return queryFactory.selectFrom(acomSvcInfo)
            .where(acomSvcInfo.svcCd.eq(svcCd),
                    acomSvcInfo.appNm.eq(appNm),
                    acomSvcInfo.apclStrDt.loe(applDt).and(acomSvcInfo.apclEndDt.goe(applDt)))
            .fetchOne();
  }

  public default List<String> findAppAcomSvcInfo(String appNm, LocalDate applDt) throws Exception
  {
    QAcomSvcInfo acomSvcInfo = new QAcomSvcInfo("acomSvcInfo");
    return queryFactory.select(acomSvcInfo.svcCd)
            .from(acomSvcInfo)
            .where(acomSvcInfo.appNm.eq(appNm),
                    acomSvcInfo.useYn.eq("Y"),
                    acomSvcInfo.apclStrDt.loe(applDt).and(acomSvcInfo.apclEndDt.goe(applDt)))
            .fetch();
  }

}