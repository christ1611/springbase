package com.springbase.core.util;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;

/**
 * JSON 형태로 Nested Object까지 표현되는 ToStringStyle
 */
public class JsonRecursiveToStringStyle extends RecursiveToStringStyle {

    public JsonRecursiveToStringStyle() {
        super();
        this.setUseClassName(false);              // 클래스명 제외
        this.setUseIdentityHashCode(false);       // @hashcode 제외
        this.setUseFieldNames(true);              // 필드명 표시
        this.setContentStart("{");                // 시작 구분자
        this.setFieldSeparator(",");              // 필드 구분자
        this.setFieldNameValueSeparator(":");     // 이름-값 구분자
        this.setContentEnd("}");                  // 끝 구분자
        this.setArrayStart("[");
        this.setArrayEnd("]");
        this.setArraySeparator(",");
    }

}