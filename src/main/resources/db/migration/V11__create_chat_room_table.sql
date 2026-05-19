CREATE TABLE chat_room (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    announcement_id   BIGINT       NOT NULL,
    room_name         VARCHAR(255) NOT NULL,
    last_msg_content  TEXT         DEFAULT NULL,
    last_msg_at       DATETIME(6)  DEFAULT NULL,
    participant_count INT          NOT NULL DEFAULT 0,
    created_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_chat_room_announcement FOREIGN KEY (announcement_id) REFERENCES announcement_common (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
