package com.parsentag.repository;

public interface ImageSourceRepository {
    byte[] findByFileId(String fileId);
}
