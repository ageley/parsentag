package com.parsentag.service.model;

import com.parsentag.service.exception.UnknownCommandException;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.function.Function;

public class CommandPayloadMapper implements Function<Message, CommandPayload> {

    @Override
    public CommandPayload apply(Message message) {
        long userId = message.getFrom()
                .getId();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        Command command = Command.fromValue(message.getText());

        if (command == null) {
            throw new UnknownCommandException(message.getText());
        }

        return CommandPayload.builder()
                .userId(userId)
                .chatId(chatId)
                .messageId(messageId)
                .command(command)
                .build();
    }

}
