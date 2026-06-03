package com.example.demo.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> handlerBusiness(BusinessException e){
        log.error("BusinessException : {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValid(MethodArgumentNotValidException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        String errorMessage = (e.getBindingResult().getFieldError() != null)
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : errorCode.getMessage();
        log.error("MethodArgumentNotValidException : {}", errorMessage);
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorMessage));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handlerBadCredentials(BadCredentialsException e){
        ErrorCode errorCode = ErrorCode.LOGIN_FAILED;
        String errorMessage = errorCode.getMessage();
        log.error("BadCredentialsException : {}", errorMessage);
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorMessage));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerHttpMessageNotReadable(HttpMessageNotReadableException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        log.error("HttpMessageNotReadableException : {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handlerMissingServletRequestParameter(MissingServletRequestParameterException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        log.error("MissingServletRequestParameterException : {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorResponse> handlerBind(BindException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        String errorMessage = (e.getBindingResult().getFieldError() != null)
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : errorCode.getMessage();
        log.error("BindException : {}", errorMessage);
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorMessage));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e){
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse("METHOD_NOT_ALLOWED", e.getMessage()));
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e){
        log.error("HttpMediaTypeNotSupportedException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponse("UNSUPPORTED_MEDIA_TYPE", e.getMessage()));
    }

    @ExceptionHandler(value = PessimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handlerPessimisticLockingFailure(PessimisticLockingFailureException e){
        ErrorCode errorCode = ErrorCode.LOCK_TIMEOUT;
        log.error("PessimisticLockingFailureException : {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handlerDataIntegrityViolation(DataIntegrityViolationException e){
        ErrorCode errorCode = ErrorCode.DUPLICATE_DATA;
        log.error("DataIntegrityViolationException : {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNoResourceFound(NoResourceFoundException e){
        log.error("NoResourceFoundException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("RESOURCE_NOT_FOUND", "요청하신 경로를 찾을 수 없습니다."));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception e){
        log.error("Exception : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내 오류가 발생했습니다."));
    }
}
