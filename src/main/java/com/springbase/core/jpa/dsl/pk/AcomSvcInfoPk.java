package com.springbase.core.jpa.dsl.pk;

import lombok.*;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false) //lombok
public class AcomSvcInfoPk implements Serializable {
    @Column(name = "APP_NM", length = 20, nullable = false)
    private String appNm;

    @Column(name = "SVC_CD", length = 20, nullable = false)
    private String svcCd;

    @Column(name = "APCL_STR_DT", nullable = false)
    private LocalDate apclStrDt;

    @Column(name = "APCL_END_DT", nullable = false)
    private LocalDate apclEndDt;
}
