package com.parsentag.repository.config;

import com.parsentag.repository.CharacterRecognitionRepository;
import com.parsentag.repository.CharacterRecognitionRepositoryTextractImpl;
import com.parsentag.repository.ImageSourceRepository;
import com.parsentag.repository.ImageSourceRepositoryTelegramImpl;
import com.parsentag.repository.ImageTargetRepository;
import com.parsentag.repository.ImageTargetRepositoryS3Impl;
import com.parsentag.repository.TextRepository;
import com.parsentag.repository.TextRepositoryS3Impl;
import com.parsentag.wrapper.S3ClientWrapper;
import com.parsentag.wrapper.TelegramClientWrapper;
import com.parsentag.wrapper.TextractClientWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RepositoryConfiguration {
    @Value("${aws.s3.image-bucket-name}")
    private final String imageBucketName;
    @Value("${aws.s3.text-bucket-name}")
    private final String textBucketName;
    @Value("${telegram.bot.token}")
    private final String token;

    @Bean
    public ImageSourceRepository imageSourceRepository(TelegramClientWrapper telegramClientWrapper) {
        return new ImageSourceRepositoryTelegramImpl(token, telegramClientWrapper);
    }

    @Bean
    public ImageTargetRepository imageTargetRepository(S3ClientWrapper s3ClientWrapper) {
        return new ImageTargetRepositoryS3Impl(imageBucketName, s3ClientWrapper);
    }

    @Bean
    public CharacterRecognitionRepository characterRecognitionRepository(TextractClientWrapper textractClientWrapper) {
        return new CharacterRecognitionRepositoryTextractImpl(imageBucketName, textractClientWrapper);
    }

    @Bean
    public TextRepository textRepository(S3ClientWrapper s3ClientWrapper) {
        return new TextRepositoryS3Impl(textBucketName, s3ClientWrapper);
    }
}
