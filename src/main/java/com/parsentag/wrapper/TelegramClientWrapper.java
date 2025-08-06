package com.parsentag.wrapper;

import com.parsentag.wrapper.exception.TelegramIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class TelegramClientWrapper {
    private final TelegramClient telegramClient;

    public <T extends Serializable, M extends BotApiMethod<T>> T execute(M method) {
        log.debug("BotApiMethod: {}", method);
        T result;

        try {
            result = telegramClient.execute(method);
        } catch (TelegramApiException e) {
            throw new TelegramIntegrationException(e);
        }

        log.debug("Telegram call result: {}", result);
        return result;
    }
}
