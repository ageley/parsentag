package com.parsentag.dispatcher;

import com.parsentag.service.ImageProcessingService;
import com.parsentag.service.MessageSendingService;
import com.parsentag.service.TaggingService;
import com.parsentag.service.model.ChosenTagPayload;
import com.parsentag.service.model.ChosenTagPayloadMapper;
import com.parsentag.service.model.CommandPayload;
import com.parsentag.service.model.CommandPayloadMapper;
import com.parsentag.service.model.ImagePayload;
import com.parsentag.service.model.ImagePayloadMapper;
import com.parsentag.service.model.NewTagPayload;
import com.parsentag.service.model.NewTagPayloadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MessageDispatcher {
    private static final String LINE_BREAK = "\n";
    private static final String START_MESSAGE = """
            Hi there!
            Please send me a single photo to extract text from.
            Or send a new #tag to be added to a list.
            Or pick a command:
            /tags - Show my #tags
            /clear - Clear all #tags
            """;

    private final ImagePayloadMapper imagePayloadMapper;
    private final ChosenTagPayloadMapper chosenTagPayloadMapper;
    private final NewTagPayloadMapper newTagPayloadMapper;
    private final CommandPayloadMapper commandPayloadMapper;
    private final MessageSendingService sender;
    private final ImageProcessingService imageProcessingService;
    private final TaggingService taggingService;

    //todo: Применить паттерн стратегия
    public void dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            processChosenTag(callback);
        } else if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasPhoto()) {
                processImage(message);
            } else if (message.hasText()) {
                String messageText = message.getText();

                if (messageText.startsWith("#")) {
                    processNewTag(message);
                } else if (messageText.startsWith("/")) {
                    processCommand(message);
                }
            }
        }
    }

    private void processCommand(Message message) {
        CommandPayload payload;

        try {
            payload = commandPayloadMapper.apply(message);
        } catch (Exception e) {
            log.info("Can't process a command", e);
            return;
        }

        switch (payload.getCommand()) {
            case START:
                processStartCommand(payload.getChatId());
                break;
            case TAGS:
                processTagsCommand(payload.getChatId());
                break;
            case CLEAR:
                processClearCommand(payload.getChatId());
                break;
            default:
                break;
        }
    }

    private void processStartCommand(long chatId) {
        sender.sendMessage(chatId, START_MESSAGE);
    }

    private void processTagsCommand(long chatId) {
        List<String> tagList;

        try {
            tagList = taggingService.getTagList(chatId);
        } catch (Exception e) {
            log.info("Can't load the tag list", e);
            return;
        }

        String tagListText = String.join(LINE_BREAK, tagList);
        String messageText = String.join(LINE_BREAK, "Your tags are:", tagListText);
        sender.sendMessage(chatId, messageText);
    }

    private void processClearCommand(long chatId) {
        try {
            taggingService.clearTagList(chatId);
        } catch (Exception e) {
            log.info("Can't clear the tag list", e);
            return;
        }

        sender.sendMessage(chatId, "Got it, the tag list has been cleared.");
    }

    private void processImage(Message message) {
        ImagePayload payload;

        try {
            payload = imagePayloadMapper.apply(message);
        } catch (Exception e) {
            sender.sendErrorMessage(message.getChatId(), "Oops, something went wrong, please try again later...", e);
            return;
        }

        ReplyKeyboard keyboard;

        try {
            keyboard = taggingService.getTagKeyboard(payload);
        } catch (Exception e) {
            log.info("Can't load a tag keyboard", e);
            return;
        }

        sender.sendReplyKeyboard(message.getChatId(), "Please choose a tag:", message.getMessageId(), keyboard);

        try {
            imageProcessingService.parseAndSaveText(payload);
        } catch (Exception e) {
            log.info("Can't parse text from an image", e);
        }
    }

    private void processChosenTag(CallbackQuery callback) {
        ChosenTagPayload payload;

        try {
            payload = chosenTagPayloadMapper.apply(callback);
        } catch (Exception e) {
            log.info("Can't process a callback", e);
            return;
        }

        String messageText;

        try {
            messageText = taggingService.getTaggedText(payload);
        } catch (Exception e) {
            log.info("Can't retrieve parsed text", e);
            messageText = payload.getTag();
        }

        sender.deleteMessage(payload.getChatId(), payload.getMessageId());
        sender.sendReplyMessage(payload.getChatId(), messageText, payload.getReplyToMessageId());
    }

    private void processNewTag(Message message) {
        NewTagPayload payload;

        try {
            payload = newTagPayloadMapper.apply(message);
        } catch (Exception e) {
            log.info("Can't process a new tag", e);
            return;
        }

        try {
            taggingService.addNewTag(payload.getChatId(), payload.getTag());
        } catch (Exception e) {
            log.info("Can't add a new tag", e);
            return;
        }

        processTagsCommand(message.getChatId());
    }
}
