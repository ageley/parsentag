package com.parsentag.dispatcher.config;

import com.parsentag.dispatcher.MessageConsumer;
import com.parsentag.dispatcher.MessageDispatcher;
import com.parsentag.service.ImageProcessingService;
import com.parsentag.service.MessageSendingService;
import com.parsentag.service.TaggingService;
import com.parsentag.service.model.ChosenTagPayloadMapper;
import com.parsentag.service.model.CommandPayloadMapper;
import com.parsentag.service.model.ImagePayloadMapper;
import com.parsentag.service.model.NewTagPayloadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class DispatcherConfiguration {
    @Value("${telegram.bot.token}")
    private final String token;

    @Bean
    public MessageDispatcher messageDispatcher(ImagePayloadMapper imagePayloadMapper,
                                               ChosenTagPayloadMapper chosenTagPayloadMapper,
                                               NewTagPayloadMapper newTagPayloadMapper,
                                               CommandPayloadMapper commandPayloadMapper,
                                               MessageSendingService messageSendingService,
                                               ImageProcessingService imageProcessingService,
                                               TaggingService taggingService) {
        return new MessageDispatcher(imagePayloadMapper, chosenTagPayloadMapper, newTagPayloadMapper,
                commandPayloadMapper, messageSendingService, imageProcessingService, taggingService);
    }

    @Bean
    public MessageConsumer messageConsumer(MessageDispatcher messageDispatcher) {
        return new MessageConsumer(token, messageDispatcher);
    }
}
