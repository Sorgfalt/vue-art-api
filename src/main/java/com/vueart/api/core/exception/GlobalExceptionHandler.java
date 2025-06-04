package com.vueart.api.core.exception;

import com.vueart.api.core.enums.Code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        log.error("Internal server error!!!.", e);
        return e;
    }

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getBindingResult(), ErrorCode.INVALID_INPUT_VALUE.getCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VueArtApiException.class)
    protected ResponseEntity<ErrorResponse> chartistException(final VueArtApiException e) {
        log.error("chartistException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 공통 예외
     */

    @ExceptionHandler(CommonNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected FailResponse handleCommonNotFoundException(HttpServletRequest request, CommonNotFoundException e) {
        return new FailResponse(ErrorCode.COMMON_NOT_FOUND_EXCEPTION.getStatus(), e.getCode());
    }

    @ExceptionHandler(CommonBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected FailResponse handleCommonBadRequestException(HttpServletRequest request, CommonBadRequestException e) {
        return new FailResponse(ErrorCode.COMMON_BAD_REQUEST_EXCEPTION.getStatus(), e.getCode());
    }

    /**
     * DB 관련 예외
     */

    @ExceptionHandler(SQLTimeoutException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected FailResponse handleSQLTimeoutException(HttpServletRequest request, SQLTimeoutException e) {
        log.error("sql timeout exception is occurred at {}", LocalDateTime.now());
        return new FailResponse(ErrorCode.SQL_TIME_OUT_EXCEPTION.getStatus(), ErrorCode.SQL_TIME_OUT_EXCEPTION.getCode());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected FailResponse handleSQLException(HttpServletRequest request, SQLException e) {
        log.error("sql exception is occurred at {}", LocalDateTime.now());
        return new FailResponse(ErrorCode.SQL_EXCEPTION.getStatus(), ErrorCode.SQL_EXCEPTION.getCode());
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected FailResponse handleBadSqlGrammarException(HttpServletRequest request, BadSqlGrammarException e) {
        log.error("bad sql grammar exception is occurred at {}, sql : {}", LocalDateTime.now(), e.getSql());
        return new FailResponse(ErrorCode.DATA_ACCESS_EXCEPTION.getStatus(), ErrorCode.DATA_ACCESS_EXCEPTION.getCode());
    }


    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected FailResponse handleDataAccessException(HttpServletRequest request, DataAccessException e) {
        String exceptionType = e.getClass().getSimpleName();
        String exceptionMessage = e.getMessage();
        log.error("data access exception is occurred, detail_exception : {}, message : {}", exceptionType, exceptionMessage, e);
        return new FailResponse(ErrorCode.DATA_ACCESS_EXCEPTION.getStatus(), ErrorCode.DATA_ACCESS_EXCEPTION.getCode());
    }


    /**
     * 전역 예외 처리
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected FailResponse handleException(HttpServletRequest request, Exception e) {
        String exceptionType = e.getClass().getSimpleName();
        String exceptionMessage = e.getMessage();
        log.error("{} is occurred, message : {}", exceptionType, exceptionMessage, e);
        return new FailResponse(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), ErrorCode.INTERNAL_SERVER_ERROR.getCode());
    }
}
