package com.parsentag.wrapper.config;

import com.parsentag.wrapper.S3ClientWrapper;
import com.parsentag.wrapper.TelegramClientWrapper;
import com.parsentag.wrapper.TextractClientWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.textract.TextractClient;

@RequiredArgsConstructor
@Configuration
public class WrapperConfiguration {
    @Value("${telegram.bot.token}")
    private final String token;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }

    @Bean
    public TelegramClientWrapper telegramClientWrapper(TelegramClient telegramClient) {
        return new TelegramClientWrapper(telegramClient);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.create();
    }

    @Bean
    public S3ClientWrapper s3ClientWrapper(S3Client s3Client) {
        return new S3ClientWrapper(s3Client);
    }

    @Bean
    public TextractClient textractClient() {
        return TextractClient.create();
    }

    @Bean
    public TextractClientWrapper textractClientWrapper(TextractClient textractClient) {
        return new TextractClientWrapper(textractClient);
    }
}