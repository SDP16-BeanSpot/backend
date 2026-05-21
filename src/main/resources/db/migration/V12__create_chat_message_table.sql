CREATE TABLE chat_message (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    room_id        BIGINT       NOT NULL,
    sender_id      BIGINT       NOT NULL,
    parent_msg_id  BIGINT       DEFAULT NULL,
    msg_type       ENUM('TALK', 'NOTICE', 'ENTER', 'QUIT') NOT NULL,
    content        TEXT         NOT NULL,
    is_deleted     TINYINT(1)   NOT NULL DEFAULT 0,
    reaction_count INT          NOT NULL DEFAULT 0,
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_chat_message_room   FOREIGN KEY (room_id)   REFERENCES chat_room (id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_message_sender FOREIGN KEY (sender_id) REFERENCES user (id)      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
