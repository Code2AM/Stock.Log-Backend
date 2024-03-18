package com.code2am.stocklog.domain.auth.common.handler;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.handler.exceptions.ValidationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(UnexpectedTypeException e) {

        String message = e.getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    // AuthUtil 관련 에러 핸들링 - authentication 관련 에러들
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleAuthUtilException(ValidationException e) {

        String message = e.getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }
}
