package com.parsentag.repository;

import com.parsentag.wrapper.TextractClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CharacterRecognitionRepositoryTextractImpl implements CharacterRecognitionRepository {
    private static final String LINE_BREAK = "\n";
    private static final String KEY_POSTFIX = ".jpg";

    private final String bucketName;
    private final TextractClientWrapper textractClientWrapper;

    private DetectDocumentTextRequest prepareDetectRequest(String key) {
        return DetectDocumentTextRequest.builder()
                .document(document -> document.s3Object(
                                object -> object.bucket(bucketName)
                                        .name(key + KEY_POSTFIX)
                        )
                )
                .build();
    }

    @Override
    public String extractTextFromImageByKey(String key) {
        return textractClientWrapper.detectDocumentText(prepareDetectRequest(key))
                .blocks()
                .stream()
                .filter(block -> block.blockType() == BlockType.LINE)
                .map(Block::text)
                .collect(Collectors.joining(LINE_BREAK));
    }
}
