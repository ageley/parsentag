package com.parsentag.service.model;

import com.parsentag.service.exception.IncorrectTagCallbackException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;

import java.util.function.Function;

public class ChosenTagPayloadMapper implements Function<CallbackQuery, ChosenTagPayload> {
    private static final String CALLBACK_DELIMITER = ":";
    private static final int CALLBACK_ELEMENTS_COUNT = 3;
    private static final int CALLBACK_TAG_PLACE = 0;
    private static final int CALLBACK_KEY_PLACE = 1;
    private static final int CALLBACK_MESSAGE_ID_PLACE = 2;

    private String[] getTagKeyPair(CallbackQuery callback) {
        String callbackData = callback.getData();
        String[] tagKeyPair = callbackData.split(CALLBACK_DELIMITER);

        if (tagKeyPair.length != CALLBACK_ELEMENTS_COUNT) {
            throw new IncorrectTagCallbackException(callbackData);
        }

        return tagKeyPair;
    }

    @Override
    public ChosenTagPayload apply(CallbackQuery callback) {
        MaybeInaccessibleMessage message = callback.getMessage();
        long userId = callback.getFrom()
                .getId();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        String[] tagKeyPair = getTagKeyPair(callback);
        String key = tagKeyPair[CALLBACK_KEY_PLACE];
        String tag = tagKeyPair[CALLBACK_TAG_PLACE];
        int replyToMessageId = Integer.parseInt(tagKeyPair[CALLBACK_MESSAGE_ID_PLACE]);

        return ChosenTagPayload.builder()
                .userId(userId)
                .chatId(chatId)
                .messageId(messageId)
                .key(key)
                .tag(tag)
                .replyToMessageId(replyToMessageId)
                .build();
    }

}
