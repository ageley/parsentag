package com.parsentag.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TagRepositoryPostgresImpl implements TagRepository {
    //language=SQL
    private static final String SELECT_SQL = "SELECT tag FROM tags WHERE chat_id = ? ORDER BY tag";
    //language=SQL
    private static final String INSERT_SQL = "INSERT INTO tags (chat_id, tag) VALUES (?, ?)";
    //language=SQL
    private static final String DELETE_SQL = "DELETE FROM tags WHERE chat_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> findAll(long chatId) {
        return jdbcTemplate.queryForList(SELECT_SQL, String.class, chatId);
    }

    @Override
    public void add(long chatId, String tag) {
        jdbcTemplate.update(INSERT_SQL, chatId, tag);
    }

    @Override
    public void removeAll(long chatId) {
        jdbcTemplate.update(DELETE_SQL, chatId);
    }
}
