package com.parsentag.repository;

import com.parsentag.wrapper.S3ClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
public class ImageTargetRepositoryS3Impl implements ImageTargetRepository {
    private static final String CONTENT_TYPE = "image/jpeg";
    private static final String KEY_POSTFIX = ".jpg";

    private final String bucketName;
    private final S3ClientWrapper s3ClientWrapper;

    private PutObjectRequest preparePutRequest(String key) {
        return PutObjectRequest.builder()
                .contentType(CONTENT_TYPE)
                .bucket(bucketName)
                .key(key + KEY_POSTFIX)
                .build();
    }

    private DeleteObjectRequest prepareDeleteRequest(String key) {
        return DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key + KEY_POSTFIX)
                .build();
    }

    @Override
    public void add(String key, byte[] imageBytes) {
        s3ClientWrapper.putObject(preparePutRequest(key), RequestBody.fromBytes(imageBytes));
    }

    @Override
    public void remove(String key) {
        s3ClientWrapper.deleteObject(prepareDeleteRequest(key));
    }
}
