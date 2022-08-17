package com.fastcampus.programming.dmaker.exception;

import com.fastcampus.programming.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e,
                                               HttpServletRequest request) {
        log.error("errorCode : {}, url : {}, message: {}",
                e.getDMakerErrorCode(),
                request.getRequestURI(),
                e.getDetailMessage());
        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    // Controller 내부로 진입하기 전 발생하는 ExceptionHandler
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(Exception e, HttpServletRequest request) {
        log.error("handleBadRequest, url : {}, message: {}", request.getRequestURI(), e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(DMakerErrorCode.INVALID_REQUEST)
                .errorMessage(DMakerErrorCode.INVALID_REQUEST.getMessage())
                .build();
    }

    // 모르는 Case를 다루기 위한 ExceptionHandler
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error("handleException, url : {}, message: {}", request.getRequestURI(), e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
                .errorMessage(DMakerErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
