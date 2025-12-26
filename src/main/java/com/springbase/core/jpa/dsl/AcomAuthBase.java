package com.springbase.core.jpa.dsl;

import com.springbase.core.jpa.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity @Table(name="ACOM_AUTH_BASE", schema="COREADM") @Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
public class AcomAuthBase extends BaseEntity implements Serializable {
   /** VARCHAR2(100)       */ @Id @Column(name="USER_ID"    , length=100             , nullable=false) private String      userId                      ;
   /** VARCHAR2(100)       */     @Column(name="PASSWORD"   , length=100             , nullable=false) private String      password                    ;
   /** VARCHAR2(100)       */     @Column(name="USER_NAME"  , length=100              ) private String      userName                    ;
   /** VARCHAR2(2)         */     @Column(name="LEDG_ST_CD" , length=2                ) private String      ledgStCd                    ;
   /** VARCHAR2(7)         */     @Column(name="ROLE" ,       length=7                ) private String      role                        ;
   /** 액세스토큰            */     @Column(name="ACCESS_TOKEN" ,length=200             ) private String      accessToken                 ;
   /** 토큰만료일자          */     @Column(name="EXP_DTM", length=100                  ) private ZonedDateTime expDtm;
}
