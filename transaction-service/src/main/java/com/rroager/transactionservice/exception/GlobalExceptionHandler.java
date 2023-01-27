package com.rroager.transactionservice.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public int handleFeignStatusException(FeignException exception, HttpServletResponse response) {
        response.setStatus(exception.status());
        return response.getStatus();
    }
}