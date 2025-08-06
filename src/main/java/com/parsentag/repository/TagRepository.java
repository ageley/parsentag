package com.parsentag.repository;

import java.util.List;

public interface TagRepository {
    List<String> findAll(long chatId);

    void add(long chatId, String tag);

    void removeAll(long chatId);
}
