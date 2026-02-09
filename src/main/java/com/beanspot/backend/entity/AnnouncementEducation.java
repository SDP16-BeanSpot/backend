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
@DiscriminatorValue("EDUCATION")
@Table(name = "announcement_education")
public class AnnouncementEducation extends Announcement {

    @Column(name = "fee")
    private Integer fee; // 참가비/비용 (Comment: 참가비/비용)

    @Column(name = "curriculum_url")
    private String curriculumUrl; // 세부 커리큘럼 (Comment: 세부 커리큘럼)
}