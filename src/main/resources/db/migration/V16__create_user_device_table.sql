CREATE TABLE user_device (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             device_id VARCHAR(255) NOT NULL,
                             fcm_token VARCHAR(255) NOT NULL,
                             created_at DATETIME(6) NOT NULL,
                             updated_at DATETIME(6) NOT NULL,
                             CONSTRAINT fk_device_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                             UNIQUE KEY uk_user_device (user_id, device_id) -- 한 유저당 기기별로 하나만 저장
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;