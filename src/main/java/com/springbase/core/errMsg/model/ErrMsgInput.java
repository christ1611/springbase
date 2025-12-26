package com.springbase.core.errMsg.model;

import com.springbase.core.common.model.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrMsgInput extends BaseDto {

    private String errCd;
    private String lnggDvCd;
    private String errTypCd;
    private String useYn;
    private String bizDvCd;
    private String errMsg;
    private String errDesc;


}
