package com.parsentag.service.model;

import com.parsentag.service.exception.ImageNotFoundException;
import com.parsentag.service.exception.ImageTooBigException;
import com.parsentag.service.exception.TooManyImagesUploadedException;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class ImagePayloadMapper implements Function<Message, ImagePayload> {
    private static final String KEY_TEMPLATE = "%d_%d_%d";

    private final int imageMaxSizesCount;
    private final int imageMaxFileSizeBytes;

    private String getMaxPhotoSizeFileId(Message message) {
        List<PhotoSize> photoSizes = message.getPhoto();

        if (photoSizes.size() > imageMaxSizesCount) {
            throw new TooManyImagesUploadedException(imageMaxSizesCount, photoSizes.size());
        }

        PhotoSize photoSize = photoSizes.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow(ImageNotFoundException::new);

        if (photoSize.getFileSize() > imageMaxFileSizeBytes) {
            throw new ImageTooBigException(imageMaxFileSizeBytes, photoSize.getFileSize());
        }

        return photoSize.getFileId();
    }

    @Override
    public ImagePayload apply(Message message) {
        long userId = message.getFrom()
                .getId();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        String key = String.format(KEY_TEMPLATE, userId, chatId, messageId);
        String fileId = getMaxPhotoSizeFileId(message);

        return ImagePayload.builder()
                .userId(userId)
                .chatId(chatId)
                .messageId(messageId)
                .key(key)
                .fileId(fileId)
                .build();
    }

}
