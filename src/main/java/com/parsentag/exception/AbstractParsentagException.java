package com.parsentag.exception;

public abstract class AbstractParsentagException extends RuntimeException {
    protected AbstractParsentagException() {
        super();
    }

    protected AbstractParsentagException(String message) {
        super(message);
    }

    protected AbstractParsentagException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AbstractParsentagException(Throwable cause) {
        super(cause);
    }
}
