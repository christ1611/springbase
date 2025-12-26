package com.springbase.core.errMsg;

import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.errMsg.mapper.ErrMsgMapper;
import com.springbase.core.errMsg.model.ErrMsgInput;
import com.springbase.core.errMsg.model.ErrMsgOutput;
import com.springbase.core.exception.CoreException;
import com.springbase.core.jpa.dsl.CoreErrCd;
import com.springbase.core.jpa.repository.DaoCoreErrCd;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/errMsg")
@RequiredArgsConstructor
public class ErrMsgController {

    private final DaoCoreErrCd daoCoreErrCd;
    private final ErrMsgMapper errMsgMapper;
    @Operation(summary = "insert error message", description = "Regist")
    @PostMapping("/insert")
    public ErrMsgOutput registError(@RequestBody @Valid ErrMsgInput input) throws Exception {
        log.debug("[START] Register Error Message");

        ErrMsgOutput errMsgOutput = new ErrMsgOutput();
        if (ObjectUtils.isNotEmpty(daoCoreErrCd.findActiveErrorCode(input.getErrCd(),input.getLnggDvCd(), 0L, "Y")))
            throw new CoreException(CoreErrCode.UNKNOWN_SYSTEM_ERROR);

        CoreErrCd coreErrCd = errMsgMapper.toCoreErrCd(input);
        coreErrCd.setUseYn("Y");
        coreErrCd.setSeqNo(0L);
        daoCoreErrCd.insert(coreErrCd);

        log.debug("[END] Register Error Message");

        return errMsgOutput;
    }


    @PostMapping("/update")
    public ErrMsgOutput updateError(@RequestBody @Valid ErrMsgInput input) throws Exception {
        log.debug("[START] Update Error Message");

        ErrMsgOutput errMsgOutput = new ErrMsgOutput();
        CoreErrCd coreErrCd = errMsgMapper.toCoreErrCd(input);
        CoreErrCd oldCoreErrCd = daoCoreErrCd.findActiveErrorCode(input.getErrCd(),input.getLnggDvCd(), 0L, null);

        if (!coreErrCd.getUseYn().equals(oldCoreErrCd.getUseYn()))
        {

            oldCoreErrCd.setUseYn(input.getUseYn());
            //change useYn only
            daoCoreErrCd.update(oldCoreErrCd);
        }
        else
        {
            oldCoreErrCd.setSeqNo(daoCoreErrCd.findMaxSeqNo(input.getErrCd(),input.getLnggDvCd())+1);
            oldCoreErrCd.setUseYn("N");
            daoCoreErrCd.insert(oldCoreErrCd);

            coreErrCd.setSeqNo(0L);
            daoCoreErrCd.update(coreErrCd);
            log.debug("[END] Uppdate Error Message");
        }




        return errMsgOutput;
    }
}
