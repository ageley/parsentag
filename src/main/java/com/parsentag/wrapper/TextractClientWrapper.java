package com.parsentag.wrapper;

import com.parsentag.wrapper.exception.AwsIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;

@Slf4j
@RequiredArgsConstructor
public class TextractClientWrapper {
    private final TextractClient textractClient;

    public DetectDocumentTextResponse detectDocumentText(DetectDocumentTextRequest request) {
        log.debug("DetectDocumentTextRequest: {}", request);
        DetectDocumentTextResponse response;

        try {
            response = textractClient.detectDocumentText(request);
        } catch (SdkException e) {
            throw new AwsIntegrationException(e);
        }

        log.debug("DetectDocumentTextResponse: {}", response);
        return response;
    }
}
