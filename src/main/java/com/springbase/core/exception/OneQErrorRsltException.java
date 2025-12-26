package com.springbase.core.exception;


import com.springbase.core.common.define.CoreErrCode;

import java.util.Formatter;

/**
 * packageName : com.oneqoncore.core.exception
 * fileName : OneQErrorRsltException
 * author : nayoseph
 * date : 2024-08-26
 * description :
 * ===========================================================
 * DATE           AUTHOR       NOTE
 * -----------------------------------------------------------
 * 2024-08-26     nayoseph      New
 */

public class OneQErrorRsltException extends CoreException {
    private static final long serialVersionUID = 1979720683723232927L;

    private static final CoreErrCode DEFAULT_ERROR_CODE = CoreErrCode.UNKNOWN_SYSTEM_ERROR;
    public OneQErrorRsltException() {
        super(DEFAULT_ERROR_CODE.getDesc());
    }

    public OneQErrorRsltException(String defErrMsgId) {
        super(defErrMsgId);
    }

    public OneQErrorRsltException(String defErrMsgId, Long failCount) {
        super(defErrMsgId, failCount);
    }

    public OneQErrorRsltException(String defErrMsgId, String subMessagee) {
        super(defErrMsgId, subMessagee);
    }
    public OneQErrorRsltException(String defErrMsgId, Long failCount, String subMessagee) {
        super(defErrMsgId, failCount, subMessagee);
    }

    public OneQErrorRsltException(String defErrMsgId, String msgFmt, Object... args) {
        super(defErrMsgId, new Formatter().format(msgFmt, args).toString());
    }

    public OneQErrorRsltException(Throwable cause) {
        super(DEFAULT_ERROR_CODE.getDesc(), cause);
    }

    public OneQErrorRsltException(String defErrMsgId, Throwable cause) {
        super(defErrMsgId, cause);
    }
}
