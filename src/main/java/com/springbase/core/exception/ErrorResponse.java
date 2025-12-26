package com.springbase.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbase.core.common.define.CoreErrCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Getter @Setter @ToString
public class ErrorResponse {

    private Integer       errorCode  = 0 ;
    private String        errorCd ;
    private String        failCount  = "";
    private String        message    = "";
    private List<String> subMessage = new ArrayList<>();
    private LocalDateTime timeStamp  = LocalDateTime.now();
    private String        globId     = "";


    @JsonIgnore
    private Throwable exception;


    public void addSubMessage(String subMsg)
    {
        if(subMessage == null) subMessage = new ArrayList<>();
        subMessage.add( subMsg );
    }
    public List<String> getSubMessage()
    {
        if(subMessage == null) return new ArrayList<>();
        return subMessage;
    }
    public String getGlobId()
    {
        if(globId == null) return "";
        return globId;
    }

    public String getErrorName()
    {
        if(exception == null) return "";
        return exception.getClass().getSimpleName();
    }
    public String getError()
    {
        if(exception == null) return "";
        return exception.getClass().getSimpleName() +":"+ exceptionToString(exception);
    }

    public String getErrorCause()
    {
        if(exception == null) return "";
        Throwable t = exception.getCause();
        if(t == null) return "";
        return "Cause - " +t.getClass().getSimpleName()+":"+exceptionToString(t);
    }

    public String getErrorCode()
    {
        return String.format("%d", errorCode);
    }



    public void setErrorCodeId(CoreErrCode coreErrCode) {
        setErrorCode(ExceptionMsg.getCode(coreErrCode.getDesc()));
    }

    public void setMessageId(CoreErrCode coreErrCode) {
        setMessage(ExceptionMsg.getMessage(coreErrCode.getDesc()));
    }


    private String exceptionToString(Throwable ex)
    {
        if(ex == null) return "";

        if( ex.getStackTrace() != null && ex.getStackTrace().length > 0 ) {
            for (StackTraceElement ste:ex.getStackTrace()) {
                if(StringUtils.startsWith(ste.getClassName(),"com.tifscore"))
                {
                    return String.format("%s : %d", ste.getClassName(), ste.getLineNumber() );
                }
            }
            StackTraceElement ste = ex.getStackTrace()[0];
            return String.format("%s : %d", ste.getClassName(), ste.getLineNumber() );
        }
        return "";
    }
}
