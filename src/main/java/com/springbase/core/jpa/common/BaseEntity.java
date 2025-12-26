package com.springbase.core.jpa.common;

import com.springbase.core.common.model.SysInfo;
import com.springbase.core.util.JsonRecursiveToStringStyle;
import com.springbase.core.util.SafeReflectionToStringBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName : com.oneqoncore.jpa.common
 * fileName : BaseEntity
 * author :
 * date :
 * description : BaseEntity 설정
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 *
 */

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@RequiredArgsConstructor
public class BaseEntity implements Cloneable
{

    @PrePersist
    public void onCreate() {
        this.sysRegDtm = LocalDateTime.now();
        if (StringUtils.isNotBlank(SysInfo.getCtxUserId()))
        {
            this.regEmpNo = SysInfo.getCtxUserId();
            this.updEmpNo = SysInfo.getCtxUserId();
        }

        this.sysUpdDtm = LocalDateTime.now();

    }

    @PreUpdate
    public void onUpdate() {
        this.sysUpdDtm = LocalDateTime.now();
        if (StringUtils.isNotBlank(SysInfo.getCtxUserId()))
        {
            this.updEmpNo = SysInfo.getCtxUserId();
        }
    }
    /**
     * 등록직원번호
     * VARCHAR2(7)
     */
    @CreatedBy
    @Column(name="REG_EMP_NO" , length=7 , nullable=false , updatable=false )
    protected String regEmpNo;

    /**
     * 시스템등록일시
     * TIMESTAMP(6) WITH TIME ZONE
     */
    @CreatedDate
    @Column(name="SYS_REG_DTM", nullable=false , updatable=false )
    protected LocalDateTime sysRegDtm;

    /**
     * 변경직원번호
     * VARCHAR2(7)
     * */
    @LastModifiedBy
    @Column(name="UPD_EMP_NO" , length=7 , nullable=false )
    protected String updEmpNo;

    /**
     * 시스템변경일시
     * TIMESTAMP(6) WITH TIME ZONE
     */
    @LastModifiedDate
    @Column(name="SYS_UPD_DTM", nullable=false )
    protected LocalDateTime sysUpdDtm;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return new SafeReflectionToStringBuilder(this, new JsonRecursiveToStringStyle()).toString();
    }
}
