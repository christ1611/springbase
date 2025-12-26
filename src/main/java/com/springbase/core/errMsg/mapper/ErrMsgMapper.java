package com.springbase.core.errMsg.mapper;

import com.springbase.core.errMsg.model.ErrMsgInput;
import com.springbase.core.jpa.dsl.CoreErrCd;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErrMsgMapper {
    CoreErrCd toCoreErrCd(ErrMsgInput errMsgInput) throws Exception;

}
