package com.parsentag.service;

import com.parsentag.wrapper.TelegramClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Slf4j
@RequiredArgsConstructor
public class MessageSendingService {
    private static final boolean ALLOW_SENDING_WITHOUT_REPLY = true;

    private final TelegramClientWrapper telegramClientWrapper;

    private SendMessage prepareSendMessage(long chatId, String messageText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    private SendMessage prepareReplyMessage(long chatId, String messageText, int replyToMessageId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyToMessageId(replyToMessageId)
                .allowSendingWithoutReply(ALLOW_SENDING_WITHOUT_REPLY)
                .build();
    }

    private SendMessage prepareReplyKeyboard(long chatId, String messageText, int replyToMessageId,
                                             ReplyKeyboard keyboard) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyToMessageId(replyToMessageId)
                .allowSendingWithoutReply(ALLOW_SENDING_WITHOUT_REPLY)
                .replyMarkup(keyboard)
                .build();
    }

    private DeleteMessage prepareDeleteMessage(long chatId, int messageId) {
        return DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }

    private void trySendMessage(SendMessage sendMessage) {
        try {
            telegramClientWrapper.execute(sendMessage);
        } catch (Exception e) {
            log.info("Can't send a message", e);
        }
    }

    private void tryDeleteMessage(DeleteMessage deleteMessage) {
        try {
            telegramClientWrapper.execute(deleteMessage);
        } catch (Exception e) {
            log.info("Can't delete a message", e);
        }
    }

    public void sendMessage(long chatId, String messageText) {
        trySendMessage(prepareSendMessage(chatId, messageText));
    }

    public void sendErrorMessage(long chatId, String messageText, Throwable e) {
        log.info("An error occurred", e);
        sendMessage(chatId, messageText);
    }

    public void sendReplyMessage(long chatId, String messageText, int replyToMessageId) {
        trySendMessage(prepareReplyMessage(chatId, messageText, replyToMessageId));
    }

    public void sendReplyKeyboard(long chatId, String messageText, int replyToMessageId, ReplyKeyboard keyboard) {
        trySendMessage(prepareReplyKeyboard(chatId, messageText, replyToMessageId, keyboard));
    }

    public void deleteMessage(long chatId, int messageId) {
        tryDeleteMessage(prepareDeleteMessage(chatId, messageId));
    }
}
