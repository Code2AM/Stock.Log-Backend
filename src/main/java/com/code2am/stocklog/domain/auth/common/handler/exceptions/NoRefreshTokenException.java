package com.code2am.stocklog.domain.auth.common.handler.exceptions;

public class NoRefreshTokenException extends RuntimeException {

    public NoRefreshTokenException() {
        super();
    }

    public NoRefreshTokenException(String message) {
        super(message);
    }

    public NoRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRefreshTokenException(Throwable cause) {
        super(cause);
    }
}