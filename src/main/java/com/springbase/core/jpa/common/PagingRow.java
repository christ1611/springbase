package com.springbase.core.jpa.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public abstract class PagingRow {

    @JsonIgnore
    private Long pgTotCnt = 0L;

}
