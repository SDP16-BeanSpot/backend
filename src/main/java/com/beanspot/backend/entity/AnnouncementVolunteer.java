package com.beanspot.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("VOLUNTEER") // 부모 테이블(announcement)의 type 컬럼에 저장될 값
@Table(name = "announcement_volunteer") // ERD에 정의된 테이블명
public class AnnouncementVolunteer extends Announcement {

    @Column(name = "is_service_hours_verified")
    private Boolean isServiceHoursVerified; // 봉사시간 인정 여부 (Comment: 봉사시간 인정 여부)

    @Column(name = "fee")
    private Integer fee; // 참가비 (Comment: 참가비)
}
