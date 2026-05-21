-- 2. 알림 설정 테이블 (상세 옵션 포함)
CREATE TABLE notification_setting (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      is_bookmark_enabled BIT(1) DEFAULT 0,
                                      is_deadline_enabled BIT(1) DEFAULT 0,
                                      deadline_d7 BIT(1) DEFAULT 0,
                                      deadline_d3 BIT(1) DEFAULT 0,
                                      deadline_d1 BIT(1) DEFAULT 0,
                                      deadline_day BIT(1) DEFAULT 0,
                                      is_activity_enabled BIT(1) DEFAULT 0,
                                      activity_d7 BIT(1) DEFAULT 0,
                                      activity_d3 BIT(1) DEFAULT 0,
                                      activity_d1 BIT(1) DEFAULT 0,
                                      activity_day BIT(1) DEFAULT 0,
                                      is_todo_enabled BIT(1) DEFAULT 0,
                                      is_keyword_enabled BIT(1) DEFAULT 0,
                                      created_at DATETIME(6) NOT NULL,
                                      updated_at DATETIME(6) NOT NULL,
                                      CONSTRAINT fk_setting_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 유저 키워드 테이블
CREATE TABLE user_keyword (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              keyword VARCHAR(255) NOT NULL,
                              created_at DATETIME(6) NOT NULL,
                              updated_at DATETIME(6) NOT NULL,
                              CONSTRAINT fk_keyword_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                              UNIQUE KEY uk_user_keyword (user_id, keyword) -- 중복 키워드 방지
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 알림 내역 테이블
CREATE TABLE notification_history (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      title VARCHAR(255) NOT NULL,
                                      content TEXT NOT NULL,
                                      type VARCHAR(50) NOT NULL, -- Enum String 저장
                                      is_read BIT(1) DEFAULT 0,
                                      created_at DATETIME(6) NOT NULL,
                                      updated_at DATETIME(6) NOT NULL,
                                      CONSTRAINT fk_history_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;