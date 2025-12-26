package com.springbase.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbase.core.common.model.SysInfo;
import com.springbase.core.component.OneQBeanUtils;
import com.springbase.core.jpa.repository.ModelJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;



@Slf4j
public class ExceptionMsg
{

    @JsonIgnore
    private final ModelJPA modelJPA =  OneQBeanUtils.getBean(ModelJPA.class);

    private static MessageSource messageSource = (MessageSource) OneQBeanUtils.getBean("messageSource");

    private static final String ERR_MSG_FORMAT = "[%s] %s";

    private static boolean isExistSubMSg(String msgStr) {
        return ( msgStr.trim().length() != msgStr.indexOf("]")+1);
    }

    private static String getErrCode(String msgStr) {
        return  msgStr.substring(1, msgStr.lastIndexOf("]"));
    }

    private static String getErrSubMsg(String msgStr) {
        return  (isExistSubMSg(msgStr) ? msgStr.substring(msgStr.lastIndexOf("]")+1, msgStr.length()).trim() : "" );
    }

    private static String toCamelCase(String snakeString) {
        if (snakeString.isEmpty())
            return "";
        String camel = String.join("", Arrays.stream(snakeString.toLowerCase().split("[-_]"))
                .map(item -> item.substring(0, 1).toUpperCase() + item.substring(1)).toArray(String[]::new));
        return snakeString.substring(0, 1).toLowerCase() + camel.substring(1);
    }

    private static String toSnakeCase(String camelString) {
        if (camelString.isEmpty())  {
            return "";
        }
        return camelString.replaceAll("([a-z])([A-Z]+)", "$1_$2");
    }


    private static String getErrorMessage(String defErrMsgId) {
        return defErrMsgId;
    }

    private static String getErrorCode(String bizErrCode) {
        return getCodeMessage(bizErrCode+".code");
    }


    private static String getCodeMessage(String defErrCode) {
        log.debug(" ExceptionMsg, defErrCode = [{}]", defErrCode);
        log.debug("ExceptionMsg, SysLnggCd = [{}]", Locale.forLanguageTag(SysInfo.getCtxLnggCd()));
        return messageSource.getMessage(defErrCode, null, Locale.forLanguageTag(SysInfo.getCtxLnggCd()));

    }


    public static String getExceptionMsg(String defErrMsgId, String subMessage) {
        return String.format(ERR_MSG_FORMAT, defErrMsgId, subMessage);
    }

    public static Integer getCode(String defErrCode) {
        try {
            return Integer.parseInt(getErrorCode(defErrCode));
        }
        catch (NumberFormatException nex )
        {
            return -1;
        }
    }

    public static String getMessage(String defErrMsgId) {
        return getExceptionMsg(getErrorMessage(defErrMsgId), "");
    }

    public static String getMessage(String defErrMsgId, String subMessage) {
        return getExceptionMsg(getErrorMessage(defErrMsgId), subMessage);
    }

    public static String parseMessage(String msgStr) {

        String mainErrMsg = getErrorMessage(getErrCode(msgStr));
        String subErrMsg = getErrSubMsg(msgStr);

        return ( subErrMsg.length() > 0 ? mainErrMsg+"\n"+subErrMsg: mainErrMsg );
    }


    public static String parseArgumentMessage(String msgStr) {

        String msgFmt = getErrorMessage(getErrCode(msgStr));
        String itemNm = getErrSubMsg(msgStr);

        return new Formatter().format(msgFmt, itemNm).toString();
    }
}
