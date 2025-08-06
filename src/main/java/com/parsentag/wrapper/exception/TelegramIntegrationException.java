package com.parsentag.wrapper.exception;

import com.parsentag.exception.AbstractParsentagException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramIntegrationException extends AbstractParsentagException {
    public TelegramIntegrationException(TelegramApiException e) {
        super(e);
    }
}
