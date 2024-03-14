package com.code2am.stocklog.domain.auth.common.handler.exceptions;

public class AuthUtilException extends RuntimeException{

    public AuthUtilException() {
        super();
    }

    public AuthUtilException(String message) {
        super(message);
    }

    public AuthUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthUtilException(Throwable cause) {
        super(cause);
    }
}
