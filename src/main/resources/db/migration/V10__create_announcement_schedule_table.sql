CREATE TABLE announcement_schedule (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       announcement_id BIGINT NOT NULL,
                                       schedule_date VARCHAR(100) DEFAULT NULL,
                                       content VARCHAR(255) NOT NULL,
                                       sequence_order INT DEFAULT 0,
                                       PRIMARY KEY (id),
                                       CONSTRAINT fk_schedule_announcement FOREIGN KEY (announcement_id) REFERENCES announcement_common (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;