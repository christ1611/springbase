package com.springbase.core.jpa.dsl;

import com.springbase.core.jpa.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity @Table(name="ACOM_AUTH_PTCL", schema="COREADM") @Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
public class AcomAuthPtcl extends BaseEntity implements Serializable {
   /** VARCHAR2(100)       */ @Id @Column(name="USER_ID"    , length=100             , nullable=false) private String      userId                      ;
   /** VARCHAR2(100)       */     @Column(name="LOCK_YN"   , length=2                , nullable=false) private String      lockYn                    ;
   /** VARCHAR2(100)       */     @Column(name="FAIL_COUNT"                           ) private Long      failCount =0L                    ;
   /** VARCHAR2(2)         */     @Column(name="RESET_PWD_YN" , length=2              ) private String      resetPwdYn                    ;
   /** VARCHAR2(2)         */     @Column(name="LNGG_CD" , length=2              ) private String      lnggCd                    ;
}
