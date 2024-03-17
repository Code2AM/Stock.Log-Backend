package com.code2am.stocklog.domain.auth.common.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

    // Validation Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        StringBuilder errorMessageBuilder = new StringBuilder();

        // 모든 FieldError를 순회하며 첫 번째 errorMessage 만 반환
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessageBuilder
                    .append(fieldError.getDefaultMessage());
                    break;
        }

        String errorMessage = errorMessageBuilder.toString();

        return ResponseEntity.badRequest().body(errorMessage);
    }
}
