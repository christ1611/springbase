package com.springbase.core.exception;

import com.springbase.core.component.OneQCTX;

public class ReTranException extends Exception
{

    private static final long serialVersionUID = 523293233964068385L;

    private OneQCTX CTX;
    private Object svcOutput;
    private String msg = "Manager approval processing";



    public ReTranException(OneQCTX CTX, String msg)
    {
        this.CTX = CTX;
        this.msg = msg;
    }

    public ReTranException(OneQCTX ctx, Object svcOutput, String msg)
    {
        this.CTX = ctx;
        this.msg = msg;
    }

    public OneQCTX getCtx()
    {
        return this.CTX;
    }

    public Object getSvcOutput()
    {
        return svcOutput;
    }

    @Override
    public String getMessage()
    {
        return msg;
    }

}
