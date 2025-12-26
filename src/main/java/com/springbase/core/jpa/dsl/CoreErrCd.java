package com.springbase.core.jpa.dsl;

import com.springbase.core.jpa.common.BaseEntity;
import com.springbase.core.jpa.dsl.pk.CoreErrCdPk;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@IdClass(CoreErrCdPk.class)
@Entity
@Table(name="CORE_ERR_CD", schema="COREADM") @Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CoreErrCd extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ERR_CD", length = 9, nullable = false)
    private String errCd;

    @Id
    @Column(name = "LNGG_DV_CD", length = 2, nullable = false)
    private String lnggDvCd;

    @Id
    @Column(name = "ERR_TYP_CD", length = 1, nullable = false)
    private String errTypCd;

    @Id
    @Column(name = "SEQ_NO", nullable = false)
    private Long seqNo;


    @Column(name = "USE_YN", length = 1)
    private String useYn;

    @Column(name = "BIZ_DV_CD", length = 3)
    private String bizDvCd;

    @Column(name = "ERR_MSG", length = 200)
    private String errMsg;

    @Column(name = "ERR_DESC", length = 500)
    private String errDesc;
}
