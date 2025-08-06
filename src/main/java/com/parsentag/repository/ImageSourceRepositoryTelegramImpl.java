package com.parsentag.repository;

import com.parsentag.repository.exception.ImageDownloadException;
import com.parsentag.wrapper.TelegramClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
public class ImageSourceRepositoryTelegramImpl implements ImageSourceRepository {
    private static final String FILE_URL_TEMPLATE = "https://api.telegram.org/file/bot%s/%s";

    private final String token;
    private final TelegramClientWrapper telegramClientWrapper;

    private GetFile prepareGetFileRequest(String fileId) {
        return new GetFile(fileId);
    }

    @Override
    public byte[] findByFileId(String fileId) {
        File file = telegramClientWrapper.execute(prepareGetFileRequest(fileId));
        String fileUrl = String.format(FILE_URL_TEMPLATE, token, file.getFilePath());

        try (InputStream inputStream = new URI(fileUrl)
                .toURL()
                .openStream()
        ) {
            return inputStream.readAllBytes();
        } catch (IOException | URISyntaxException e) {
            throw new ImageDownloadException(e);
        }
    }
}
