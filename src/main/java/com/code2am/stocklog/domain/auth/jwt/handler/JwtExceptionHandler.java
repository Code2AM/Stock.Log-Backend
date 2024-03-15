package com.code2am.stocklog.domain.auth.jwt.handler;

import com.code2am.stocklog.domain.auth.jwt.handler.exceptions.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class JwtExceptionHandler {

    // jwt 토큰 관련된 에러 핸들러
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 상태 코드 설정
    @ResponseBody
    public ResponseEntity<String> handleJwtException(JwtException e) {

        System.out.println("JwtExceptionHandler");

        String message = e.getMessage();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }
}
