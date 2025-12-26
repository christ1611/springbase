package com.springbase.core.exception;

import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.component.OneQCTX;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
@Slf4j
public class CoreRestControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView prcInvalidArgumentException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("MethodArgumentNotValidException", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setException(ex);
        errResponse.setErrorCodeId(CoreErrCode.SYS_INPUT_VALIDATION_ERROR);

        for (FieldError fErr : ex.getBindingResult().getFieldErrors()) {
            String subMsg = "";
            switch (fErr.getCode()) {
                case "NotBlank":
                    subMsg = String.format("%s Required input [%s]", fErr.getDefaultMessage(), fErr.getField());
                    break;
                default:
                    subMsg = String.format("%s [%s]", fErr.getDefaultMessage(), fErr.getField());
            }
            errResponse.addSubMessage(subMsg);
        }
        errResponse.setMessageId(CoreErrCode.SYS_INPUT_VALIDATION_ERROR);

        return getResponseModel(errResponse, request);
    }

    @ExceptionHandler(NullPointerException.class)
    public ModelAndView prcNullPointerException(NullPointerException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("NullPointerException", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setException(ex);
        errResponse.setErrorCodeId(CoreErrCode.UNKNOWN_SYSTEM_ERROR);
        errResponse.setMessage("1QCore NullPointerException");
        errResponse.setSubMessage(List.of(ex.getMessage()));

        return getResponseModel(errResponse, request);
    }

    @ExceptionHandler(ServletException.class)
    public ModelAndView noHandlerFoundException(ServletException  ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("NullPointerException", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setErrorCd(String.valueOf(CoreErrCode.METHOD_ERROR.getCode()));
        errResponse.setException(ex);
        errResponse.setMessage(CoreErrCode.METHOD_ERROR.getDesc());
        errResponse.setSubMessage(List.of(ex.getMessage()));

        return getResponseModel(errResponse, request);
    }

    @ExceptionHandler(SQLGrammarException.class)
    public ModelAndView noHandlerFoundException(SQLGrammarException  ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("SQLGrammarException", ex);
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setErrorCd(String.valueOf(CoreErrCode.DB_SQL_GRAMMAR_ERROR.getCode()));
        errResponse.setException(ex);
        errResponse.setMessage(CoreErrCode.DB_SQL_GRAMMAR_ERROR.getDesc());
        errResponse.setSubMessage(List.of(ex.getMessage()));

        return getResponseModel(errResponse, request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView noHandlerFoundException(NoHandlerFoundException  ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("NullPointerException", ex);
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setErrorCd(String.valueOf(CoreErrCode.SERVICE_NOT_FOUND.getCode()));
        errResponse.setException(ex);
        errResponse.setMessage(CoreErrCode.SERVICE_NOT_FOUND.getDesc());
        errResponse.setSubMessage(List.of(ex.getMessage()));

        return getResponseModel(errResponse, request);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public void nonUniqueResultException(NonUniqueResultException  ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("NonUniqueResultException", ex);
        throw new CoreException(CoreErrCode.DB_INCORRECT_RESULT_SIZE_ERROR);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ModelAndView prcSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("SQLIntegrityConstraintViolationException", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setException(ex);
        errResponse.setErrorCode(ex.getErrorCode());
        errResponse.setErrorCd(String.valueOf(CoreErrCode.DB_DML_ERROR.getCode()));

        List<String> subMsg = new ArrayList<>();
        subMsg.add(ex.getMessage());
        subMsg.add(ex.getSQLState());
        SQLException nex = ex.getNextException();
        if (nex != null) {
            subMsg.add(nex.getMessage());
        }
        errResponse.setSubMessage(subMsg);

        return getResponseModel(errResponse, request);
    }


    @ExceptionHandler(PersistenceException.class)
    public ModelAndView prcPersistenceException(PersistenceException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("PersistenceException", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setErrorCd(String.valueOf(CoreErrCode.DB_DML_ERROR.getCode()));
        errResponse.setGlobId(OneQCTX.getGlobalId());
        errResponse.setTimeStamp(LocalDateTime.now());
        Throwable rcex = getCauseClassException(ex, SQLIntegrityConstraintViolationException.class);
        if (rcex != null) {
            errResponse.setException(rcex);
            List<String> subMsg = new ArrayList<>();
            subMsg.add(ex.getMessage());
            errResponse.setSubMessage(subMsg);
        } else {
            errResponse.setException(ex);
        }

        return getResponseModel(errResponse, request);
    }





    @ExceptionHandler(value = {CoreException.class})
    public ModelAndView prcOneQOnCoreException(CoreException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("OneQOnCoreException", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return getResponseModel(ex.getErrorResponse(), request);
    }




    public ModelAndView getResponseModel(ExtErrorResponse response, HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsonView");

        mv.addObject("GlobalId", response.getGlobId());
        mv.addObject("sysInfo", OneQCTX.getCtxSysInfo().getOutMap());
        mv.addObject("output", response);
        return mv;
    }
    public ModelAndView getResponseModel(ErrorResponse response, HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsonView");

        mv.addObject("GlobalId", response.getGlobId());
        mv.addObject("sysInfo", OneQCTX.getCtxSysInfo().getOutMap());
        mv.addObject("output", response);
        return mv;
    }


    private Throwable getCauseClassException(Throwable ex, Class<?> cls) {
        Throwable r_ex = ex.getCause();
        if (r_ex != null) {
            if (cls.isInstance(r_ex)) {
                return r_ex;
            } else {
                r_ex = getCauseClassException(r_ex, cls);
            }
        }
        return r_ex;
    }


    @ExceptionHandler(ReTranException.class)
    public ModelAndView ReTranException(ReTranException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("ReTranException [{}]", ex.getMessage());

        ModelAndView mv = new ModelAndView("jsonView");
        response.setStatus(HttpStatus.OK.value());
        OneQCTX CTX = ex.getCtx();
        mv.addObject("GlobalID", CTX.getGlobId());
//        mv.addObject("SysInfo", CTX.getSysInfo().getOutMap());

        log.debug("ReTranException output[{}]", ex.getSvcOutput());

        if (ex.getSvcOutput() != null) {
            mv.addObject("output", ex.getSvcOutput());
        }
        log.debug("return Model Map [{}]", mv.getModelMap());
        return mv;
    }
}
