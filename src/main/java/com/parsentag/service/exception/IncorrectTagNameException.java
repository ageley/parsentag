package com.parsentag.service.exception;

import com.parsentag.exception.AbstractParsentagException;

public class IncorrectTagNameException extends AbstractParsentagException {
    public IncorrectTagNameException(String tag) {
        super(String.format("Incorrect tag name: %s", tag));
    }
}
