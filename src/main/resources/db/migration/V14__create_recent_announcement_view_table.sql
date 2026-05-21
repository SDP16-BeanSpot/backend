CREATE TABLE recent_announcement_view (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    announcement_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_recent_view_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_recent_view_announcement FOREIGN KEY (announcement_id) REFERENCES announcement_common(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_announcement_view (user_id, announcement_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;