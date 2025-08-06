package com.parsentag.service;

import com.parsentag.repository.TagRepository;
import com.parsentag.repository.TextRepository;
import com.parsentag.service.model.ChosenTagPayload;
import com.parsentag.service.model.ImagePayload;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TaggingService {
    private static final String CALLBACK_TEMPLATE = "%s:%s:%d";
    private static final String LINE_BREAK = "\n";
    private static final String NO_TAG = "No tag";

    private final TagRepository tagRepository;
    private final TextRepository textRepository;

    private String getCallbackData(String tag, ImagePayload payload) {
        return String.format(CALLBACK_TEMPLATE, tag, payload.getKey(), payload.getMessageId());
    }

    public ReplyKeyboard getTagKeyboard(ImagePayload payload) {
        List<InlineKeyboardRow> tagButtons = new ArrayList<>();

        for (String tag : tagRepository.findAll(payload.getChatId())) {
            InlineKeyboardButton tagButton = InlineKeyboardButton.builder()
                    .text(tag)
                    .callbackData(getCallbackData(tag, payload))
                    .build();
            tagButtons.add(new InlineKeyboardRow(tagButton));
        }

        InlineKeyboardButton noTagButton = InlineKeyboardButton.builder()
                .text(NO_TAG)
                .callbackData(getCallbackData(NO_TAG, payload))
                .build();
        tagButtons.add(new InlineKeyboardRow(noTagButton));
        return InlineKeyboardMarkup.builder()
                .keyboard(tagButtons)
                .build();
    }

    public String getTaggedText(ChosenTagPayload payload) {
        String parsedText;

        try {
            parsedText = textRepository.findByKey(payload.getKey());
        } finally {
            textRepository.remove(payload.getKey());
        }

        String taggedText;

        if (NO_TAG.equals(payload.getTag())) {
            taggedText = parsedText;
        } else {
            taggedText = String.join(LINE_BREAK, parsedText, payload.getTag());
        }

        return taggedText;
    }

    public List<String> getTagList(long chatId) {
        return tagRepository.findAll(chatId);
    }

    public void addNewTag(long chatId, String tag) {
        tagRepository.add(chatId, tag);
    }

    public void clearTagList(long chatId) {
        tagRepository.removeAll(chatId);
    }
}
