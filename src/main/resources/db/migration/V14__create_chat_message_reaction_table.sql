CREATE TABLE chat_message_reaction (
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    msg_id        BIGINT      NOT NULL,
    user_id       BIGINT      NOT NULL,
    reaction_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reaction_message FOREIGN KEY (msg_id)   REFERENCES chat_message (id) ON DELETE CASCADE,
    CONSTRAINT fk_reaction_user    FOREIGN KEY (user_id) REFERENCES user (id)          ON DELETE CASCADE,
    CONSTRAINT uq_reaction         UNIQUE (msg_id, user_id, reaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
