package com.parsentag.service.exception;

import com.parsentag.exception.AbstractParsentagException;

public class IncorrectTagCallbackException extends AbstractParsentagException {
    public IncorrectTagCallbackException(String callbackData) {
        super(String.format("Incorrect tag callback data: %s", callbackData));
    }
}
