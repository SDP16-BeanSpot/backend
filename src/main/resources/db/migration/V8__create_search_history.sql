CREATE TABLE search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    keyword VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    CONSTRAINT uk_user_keyword UNIQUE (user_id, keyword),
    INDEX idx_user_updated (user_id, updated_at DESC),
    CONSTRAINT fk_search_history_user
        FOREIGN KEY (user_id) REFERENCES user(id)
);