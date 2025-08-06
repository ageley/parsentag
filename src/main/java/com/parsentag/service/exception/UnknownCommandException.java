package com.parsentag.service.exception;

import com.parsentag.exception.AbstractParsentagException;

public class UnknownCommandException extends AbstractParsentagException {
    public UnknownCommandException(String command) {
        super("An unknown command: " + command);
    }
}
