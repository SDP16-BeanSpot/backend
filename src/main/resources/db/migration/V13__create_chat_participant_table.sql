CREATE TABLE chat_participant (
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    room_id          BIGINT      NOT NULL,
    user_id          BIGINT      NOT NULL,
    role             VARCHAR(20) NOT NULL DEFAULT 'USER',
    entered_at       DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    is_notified      TINYINT(1)  NOT NULL DEFAULT 1,
    last_read_msg_id BIGINT      DEFAULT NULL,
    is_pinned        TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_chat_participant_room FOREIGN KEY (room_id) REFERENCES chat_room (id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_participant_user FOREIGN KEY (user_id) REFERENCES user (id)      ON DELETE CASCADE,
    CONSTRAINT uq_chat_participant      UNIQUE (room_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
