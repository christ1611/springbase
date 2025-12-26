package com.springbase.core.jpa.dsl.pk;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
public class CoreErrCdPk implements Serializable {

    @Column(name = "ERR_CD", length = 9, nullable = false)private String errCd;
    @Column(name = "LNGG_DV_CD", length = 2, nullable = false) private String lnggDvCd;
    @Column(name = "ERR_TYP_CD", length = 1, nullable = false) private String errTypCd;
    @Column(name = "SEQ_NO", nullable = false) private Long seqNo;
}
