package com.parsentag.wrapper;

import com.parsentag.wrapper.exception.AwsIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
@RequiredArgsConstructor
public class S3ClientWrapper {
    private final S3Client s3Client;

    public ResponseBytes<GetObjectResponse> getObjectAsBytes(GetObjectRequest request) {
        log.debug("GetObjectRequest: {}", request);
        ResponseBytes<GetObjectResponse> response;

        try {
            response = s3Client.getObjectAsBytes(request);
        } catch (SdkException e) {
            throw new AwsIntegrationException(e);
        }

        log.debug("ResponseBytes<GetObjectResponse>: {}", response);
        return response;
    }

    public void putObject(PutObjectRequest request, RequestBody body) {
        log.debug("PutObjectRequest: {}", request);
        PutObjectResponse response;

        try {
            response = s3Client.putObject(request, body);
        } catch (SdkException e) {
            throw new AwsIntegrationException(e);
        }

        log.debug("PutObjectResponse: {}", response);
    }

    public void deleteObject(DeleteObjectRequest request) {
        log.debug("DeleteObjectRequest: {}", request);
        DeleteObjectResponse response;

        try {
            response = s3Client.deleteObject(request);
        } catch (SdkException e) {
            throw new AwsIntegrationException(e);
        }

        log.debug("DeleteObjectResponse: {}", response);
    }
}
