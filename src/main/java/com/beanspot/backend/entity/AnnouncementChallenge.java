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
@DiscriminatorValue("CHALLENGE")
@Table(name = "announcement_challenge")
public class AnnouncementChallenge extends Announcement {

    @Column(name = "prize_scale")
    private String prizeScale; // 시상 규모 (Comment: 시상 규모)

    @Column(name = "participant_target")
    private String participantTarget; // 모집 대상 (Comment: 모집 대상)

    @Column(name = "team_size")
    private String teamSize; // 팀원 규모 (Comment: 팀원 규모)
}