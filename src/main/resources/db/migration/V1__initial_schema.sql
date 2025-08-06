CREATE SEQUENCE IF NOT EXISTS tag_seq;
CREATE TABLE IF NOT EXISTS tags
(
    tag_id           BIGINT    DEFAULT NEXTVAL('tag_seq') NOT NULL,
    chat_id          BIGINT                               NOT NULL,
    tag              VARCHAR(255)                         NOT NULL,
    insert_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    CONSTRAINT tag_pk PRIMARY KEY (tag_id)
);
CREATE INDEX IF NOT EXISTS tags_chat_id_idx ON tags (chat_id);
