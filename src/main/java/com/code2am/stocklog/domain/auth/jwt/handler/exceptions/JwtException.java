package com.code2am.stocklog.domain.auth.jwt.handler.exceptions;

public class JwtException extends RuntimeException{

    // 메시지를 받는 생성자
    public JwtException(String message) {
        super(message);
    }
}
