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
@DiscriminatorValue("SUPPORTER")
@Table(name = "announcement_supporter")
public class AnnouncementSupporter extends Announcement {

    @Column(name = "benefits")
    private String benefits; // 활동 혜택 (Comment: 활동 혜택)

    @Column(name = "selection_process")
    private String selectionProcess; // 심사 방식 (Comment: 심사 방식)
}