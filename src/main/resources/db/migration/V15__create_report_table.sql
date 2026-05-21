CREATE TABLE report (
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    reporter_id      BIGINT      NOT NULL,
    reported_user_id BIGINT      NOT NULL,
    message_id       BIGINT      NOT NULL,
    report_type      ENUM(
        'INAPPROPRIATE_LANGUAGE',
        'SEXUAL_LANGUAGE',
        'FINANCIAL_REQUEST',
        'ADVERTISEMENT',
        'SPAM',
        'OFFENSIVE',
        'OTHER'
    ) NOT NULL,
    status           ENUM('PENDING', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
    created_at       DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_report_reporter      FOREIGN KEY (reporter_id)      REFERENCES user (id)         ON DELETE CASCADE,
    CONSTRAINT fk_report_reported_user FOREIGN KEY (reported_user_id) REFERENCES user (id)         ON DELETE CASCADE,
    CONSTRAINT fk_report_message       FOREIGN KEY (message_id)       REFERENCES chat_message (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
