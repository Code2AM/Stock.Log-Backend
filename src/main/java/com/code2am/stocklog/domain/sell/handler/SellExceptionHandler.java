package com.code2am.stocklog.domain.sell.handler;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.sell.handler.exception.SellException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SellExceptionHandler {

    // 매도 로직 관련 에러 핸들링
    @ExceptionHandler(SellException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleAuthUtilException(AuthUtilException e) {

        String message = e.getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }
}
