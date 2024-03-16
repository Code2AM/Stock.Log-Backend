package com.code2am.stocklog.domain.sell.handler.exception;

public class SellException extends RuntimeException {

    public SellException() {
        super();
    }

    public SellException(String message) {
        super(message);
    }

    public SellException(String message, Throwable cause) {
        super(message, cause);
    }

    public SellException(Throwable cause) {
        super(cause);
    }
}
