package com.parsentag.service;

import com.parsentag.repository.CharacterRecognitionRepository;
import com.parsentag.repository.ImageSourceRepository;
import com.parsentag.repository.ImageTargetRepository;
import com.parsentag.repository.TextRepository;
import com.parsentag.service.model.ImagePayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImageProcessingService {
    private final ImageSourceRepository imageSourceRepository;
    private final ImageTargetRepository imageTargetRepository;
    private final CharacterRecognitionRepository characterRecognitionRepository;
    private final TextRepository textRepository;

    public void parseAndSaveText(ImagePayload payload) {
        byte[] imageBytes = imageSourceRepository.findByFileId(payload.getFileId());
        imageTargetRepository.add(payload.getKey(), imageBytes);
        String parsedText;

        try {
            parsedText = characterRecognitionRepository.extractTextFromImageByKey(payload.getKey());
        } finally {
            imageTargetRepository.remove(payload.getKey());
        }

        textRepository.add(payload.getKey(), parsedText);
    }
}
