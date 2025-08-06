package com.parsentag.service.model;

import com.parsentag.service.exception.IncorrectTagNameException;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.function.Function;
import java.util.regex.Pattern;

public class NewTagPayloadMapper implements Function<Message, NewTagPayload> {
    private static final Pattern TAG_PATTERN = Pattern.compile("^#\\p{L}[\\p{L}\\p{N}\\p{M}_]*$");
    private static final int MIN_TAG_LENGTH = 2;
    private static final int MAX_TAG_LENGTH = 256;

    @Override
    public NewTagPayload apply(Message message) {
        long userId = message.getFrom()
                .getId();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        String tag = message.getText();

        if (tag.length() < MIN_TAG_LENGTH
                || tag.length() > MAX_TAG_LENGTH
                || !TAG_PATTERN.matcher(tag).matches()) {
            throw new IncorrectTagNameException(tag);
        }

        return NewTagPayload.builder()
                .userId(userId)
                .chatId(chatId)
                .messageId(messageId)
                .tag(tag)
                .build();
    }

}
