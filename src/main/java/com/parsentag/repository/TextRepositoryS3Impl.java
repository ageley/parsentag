package com.parsentag.repository;

import com.parsentag.wrapper.S3ClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
public class TextRepositoryS3Impl implements TextRepository {
    private static final String CONTENT_TYPE = "text/plain";
    private static final String KEY_POSTFIX = ".txt";

    private final String bucketName;
    private final S3ClientWrapper s3ClientWrapper;

    private GetObjectRequest prepareGetRequest(String key) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key + KEY_POSTFIX)
                .build();
    }

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
    public String findByKey(String key) {
        return s3ClientWrapper.getObjectAsBytes(prepareGetRequest(key))
                .asUtf8String();
    }

    @Override
    public void add(String key, String text) {
        s3ClientWrapper.putObject(preparePutRequest(key), RequestBody.fromString(text));
    }

    @Override
    public void remove(String key) {
        s3ClientWrapper.deleteObject(prepareDeleteRequest(key));
    }
}
