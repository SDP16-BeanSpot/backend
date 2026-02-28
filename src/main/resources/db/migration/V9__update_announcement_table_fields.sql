ALTER TABLE announcement_common
    -- 누락된 컬럼 추가
    ADD COLUMN organizer_img_url VARCHAR(500) AFTER organizer,
    ADD COLUMN activity_content TEXT AFTER organizer_img_url,
    ADD COLUMN target VARCHAR(255) AFTER activity_content,
    ADD COLUMN recruitment_count VARCHAR(255) AFTER target,
    ADD COLUMN apply_method VARCHAR(255) AFTER recruitment_count,
    ADD COLUMN activity_method VARCHAR(255) AFTER recruitment_end,
    ADD COLUMN detail_content TEXT AFTER fee,
    ADD COLUMN benefits VARCHAR(255) AFTER detail_content,

    -- 기존 컬럼 데이터 타입 및 제약 조건 수정
    MODIFY COLUMN type VARCHAR(255) DEFAULT NULL; -- EnumType.STRING 대응


-- 기존에 'content'로 잘못 생성된 컬럼이 있다면 삭제 (엔티티에는 activityContent와 detailContent로 분리됨)
 ALTER TABLE announcement_common DROP COLUMN content;