package com.parsentag.repository;

public interface TextRepository {
    String findByKey(String key);

    void add(String key, String text);

    void remove(String key);
}
