CREATE TABLE announcement_common (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    type VARCHAR(30) NOT NULL,

    title VARCHAR(255) NOT NULL,
    content TEXT,
    organizer VARCHAR(255),
    fee INT,

    img_url VARCHAR(500),
    link_url VARCHAR(500),

    location VARCHAR(255) NOT NULL,
    region VARCHAR(20) NOT NULL,

    start_date DATE,
    end_date DATE,

    schedule_detail TEXT,

    recruitment_start DATE,
    recruitment_end DATE,

    view_count INT DEFAULT 0,

    lat DOUBLE,
    lng DOUBLE,

    service_hours_verified VARCHAR(50),

    selection_process TEXT,
    award_scale TEXT,
    team_size VARCHAR(50),

    created_at DATETIME,
    updated_at DATETIME
);