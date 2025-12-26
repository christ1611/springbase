package com.springbase.core.exception;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.component.OneQBeanUtils;
import com.springbase.core.component.OneQCTX;
import com.springbase.core.jpa.dsl.CoreErrCd;
import com.springbase.core.jpa.repository.ModelJPA;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * packageName : com.oneqoncore.core.exception
 * fileName : OneQOnCoreException
 * author : nayoseph
 * date : 2024-08-26
 * description :
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-08-26     nayoseph      New
 */

public class CoreException extends RuntimeException
{

    @JsonIgnore
    private final ModelJPA modelJPA =  OneQBeanUtils.getBean(ModelJPA.class);
    @Serial
    private static final long serialVersionUID = 4706127219501382258L;

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;

    protected ErrorResponse errorResponse;


    public CoreException() {
        super(ExceptionMsg.getMessage(DEFAULT_ERROR_CODE.getDesc()));
        initErrorResponse(DEFAULT_ERROR_CODE.getDesc());
    }

    public CoreException(CoreErrCode coreErrCode) {
        super(coreErrCode.getDesc());
        initErrorResponse(coreErrCode);
    }

    public CoreException(CoreErrCode coreErrCode, String subMessage) {
        super(coreErrCode.getDesc());
        initErrorResponse(coreErrCode);
        this.errorResponse.addSubMessage(subMessage);
    }

    public CoreException(String defErrMsgId) {

        super(ExceptionMsg.getMessage(defErrMsgId));
        initErrorResponse(defErrMsgId);
    }

    public CoreException(String defErrMsgId, Long failCount) {
        super(ExceptionMsg.getMessage(defErrMsgId));
        initErrorResponse(defErrMsgId, failCount);
    }


    public CoreException(String defErrMsgId, String subMessagee) {

        super(ExceptionMsg.getMessage(defErrMsgId));

        initErrorResponse(defErrMsgId);
        this.errorResponse.addSubMessage(subMessagee);
    }

    public CoreException(String defErrMsgId, Long failCount, String subMessagee) {

        super(ExceptionMsg.getMessage(defErrMsgId));

        initErrorResponse(defErrMsgId, failCount);
        this.errorResponse.addSubMessage(subMessagee);
    }


    public CoreException(String defErrMsgId, String msgFmt, Object... args) {
        super(ExceptionMsg.getMessage(defErrMsgId, String.format(msgFmt, args).toString()));

        initErrorResponse(defErrMsgId);
        this.errorResponse.addSubMessage(String.format(msgFmt, args).toString());
    }

    public CoreException(Throwable cause) {
        super(ExceptionMsg.getMessage(DEFAULT_ERROR_CODE.getDesc()), cause);
        initErrorResponse(DEFAULT_ERROR_CODE.getDesc());

        this.errorResponse.addSubMessage(cause.getMessage());
    }

    public CoreException(String defErrMsgId, Throwable cause) {
        super(ExceptionMsg.getMessage(defErrMsgId), cause);
        initErrorResponse(defErrMsgId);
    }

    private void initErrorResponse(CoreErrCode coreErrCode){
        this.errorResponse = new ErrorResponse();
        this.errorResponse.setErrorCode(coreErrCode.getCode());
        this.errorResponse.setMessage(coreErrCode.getDesc());
        this.errorResponse.setTimeStamp(LocalDateTime.now());
        this.errorResponse.setGlobId(OneQCTX.getGlobalId());
        this.errorResponse.setException(this);
    }


    private void initErrorResponse(String defErrMsgId){
        CoreErrCd errCode = modelJPA.findCoreErrorCd(defErrMsgId);
        this.errorResponse = new ErrorResponse();
        this.errorResponse.setErrorCd(errCode.getErrCd());
        this.errorResponse.setMessage(errCode.getErrMsg());
        this.errorResponse.setTimeStamp(LocalDateTime.now());
        this.errorResponse.setGlobId(OneQCTX.getGlobalId());
        this.errorResponse.setException(this);
    }

    private void initErrorResponse(String defErrMsgId, Long failCount){
        CoreErrCd errCode = modelJPA.findCoreErrorCd(defErrMsgId);
        this.errorResponse = new ErrorResponse();
        this.errorResponse.setErrorCd(errCode.getErrCd());
        this.errorResponse.setMessage(errCode.getErrMsg());
        this.errorResponse.setFailCount(failCount.toString());
        this.errorResponse.setTimeStamp(LocalDateTime.now());
        this.errorResponse.setGlobId(OneQCTX.getGlobalId());
        this.errorResponse.setException(this);
    }


    public ErrorResponse getErrorResponse()
    {
        return errorResponse;
    }
}
