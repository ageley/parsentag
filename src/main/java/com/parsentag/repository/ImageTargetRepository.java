package com.parsentag.repository;

public interface ImageTargetRepository {
    void add(String key, byte[] imageBytes);

    void remove(String key);
}
