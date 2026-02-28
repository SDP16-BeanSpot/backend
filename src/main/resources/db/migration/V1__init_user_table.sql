CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      created_at DATETIME(6) DEFAULT NULL,
                      updated_at DATETIME(6) DEFAULT NULL,
                      address VARCHAR(255) DEFAULT NULL,
                      email VARCHAR(255) DEFAULT NULL,
                      email_verified BIT(1) NOT NULL,
                      name VARCHAR(255) DEFAULT NULL,
                      nickname VARCHAR(15) NOT NULL,
                      password VARCHAR(100) DEFAULT NULL,
                      phone VARCHAR(20) DEFAULT NULL,
                      profile_url VARCHAR(255) DEFAULT NULL,
                      refresh_token VARCHAR(255) DEFAULT NULL,
                      social_type ENUM ('GOOGLE', 'KAKAO', 'NAVER', 'NONE') DEFAULT NULL,
                      social_id VARCHAR(255) DEFAULT NULL,
                      user_id VARCHAR(255) DEFAULT NULL,
                      PRIMARY KEY (id),
                      CONSTRAINT uk_user_nickname UNIQUE (nickname),
                      CONSTRAINT uk_user_phone UNIQUE (phone)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;