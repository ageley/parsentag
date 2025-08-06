package com.parsentag.service.config;

import com.parsentag.repository.CharacterRecognitionRepository;
import com.parsentag.repository.ImageSourceRepository;
import com.parsentag.repository.ImageTargetRepository;
import com.parsentag.repository.TagRepository;
import com.parsentag.repository.TextRepository;
import com.parsentag.service.ImageProcessingService;
import com.parsentag.service.MessageSendingService;
import com.parsentag.service.TaggingService;
import com.parsentag.service.model.ChosenTagPayloadMapper;
import com.parsentag.service.model.CommandPayloadMapper;
import com.parsentag.service.model.ImagePayloadMapper;
import com.parsentag.service.model.NewTagPayloadMapper;
import com.parsentag.wrapper.TelegramClientWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ServiceConfiguration {
    @Value("${image.max.sizes-count}")
    private final int imageMaxSizesCount;
    @Value("${image.max.size-mb}")
    private final int imageMaxFileSizeMb;

    @Bean
    public ImagePayloadMapper imagePayloadMapper() {
        int imageMaxFileSizeBytes = imageMaxFileSizeMb * 1024 * 1024;
        return new ImagePayloadMapper(imageMaxSizesCount, imageMaxFileSizeBytes);
    }

    @Bean
    public ChosenTagPayloadMapper chosenTagPayloadMapper() {
        return new ChosenTagPayloadMapper();
    }

    @Bean
    public NewTagPayloadMapper newTagPayloadMapper() {
        return new NewTagPayloadMapper();
    }

    @Bean
    public CommandPayloadMapper commandPayloadMapper() {
        return new CommandPayloadMapper();
    }

    @Bean
    public ImageProcessingService textParsingService(ImageSourceRepository imageSourceRepository,
                                                     ImageTargetRepository imageTargetRepository,
                                                     CharacterRecognitionRepository characterRecognitionRepository,
                                                     TextRepository textRepository) {
        return new ImageProcessingService(imageSourceRepository, imageTargetRepository, characterRecognitionRepository,
                textRepository);
    }

    @Bean
    public TaggingService taggingService(TagRepository tagRepository, TextRepository textRepository) {
        return new TaggingService(tagRepository, textRepository);
    }

    @Bean
    public MessageSendingService messageSendingService(TelegramClientWrapper telegramClientWrapper) {
        return new MessageSendingService(telegramClientWrapper);
    }
}
