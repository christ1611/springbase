package com.springbase.core.jpa.dsl;

import com.springbase.core.jpa.common.BaseEntity;
import com.springbase.core.jpa.dsl.pk.AcomSvcInfoPk;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@IdClass(AcomSvcInfoPk.class)
@Entity
@Table(name="ACOM_SVC_INFO", schema="COREADM")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AcomSvcInfo extends BaseEntity implements Serializable {
    @Id @Column(name = "APP_NM", length = 20, nullable = false)
    private String appNm;

    @Id @Column(name = "SVC_CD", length = 20, nullable = false)
    private String svcCd;

    @Id @Column(name = "APCL_STR_DT", nullable = false)
    private LocalDate apclStrDt;

    @Id @Column(name = "APCL_END_DT", nullable = false)
    private LocalDate apclEndDt;


    @Column(name = "USE_YN", length = 1)
    private String useYn;

    @Column(name = "SVC_NM", length = 100)
    private String svcNm;

    @Column(name = "LOG_FILE_YN", length = 2)
    private String logFileYn;

    @Column(name = "TRSC_KIND_CD", length = 6)
    private String trscKindCd;

    @Column(name = "URI", length = 100)
    private String uri;

}
