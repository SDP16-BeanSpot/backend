package com.beanspot.backend.service;

import com.beanspot.backend.dto.admin.AnnouncementRequestDto; // 경로 수정
import com.beanspot.backend.entity.*;
import com.beanspot.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    public Long createAnnouncement(AnnouncementRequestDto dto, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자가 없습니다."));

        // 1. 유형별 빌더 호출 (모든 엔티티에 @SuperBuilder가 있어야 함)
        Announcement announcement = switch (dto.getType().toUpperCase()) {
            case "VOLUNTEER" -> AnnouncementVolunteer.builder()
                    .isServiceHoursVerified(dto.getIsServiceHoursVerified())
                    .fee(dto.getFee())
                    .build();
            case "CHALLENGE" -> AnnouncementChallenge.builder()
                    .prizeScale(dto.getPrizeScale())
                    .participantTarget(dto.getParticipantTarget())
                    .teamSize(dto.getTeamSize())
                    .build();
            // ... 나머지 case 추가
            default -> throw new IllegalArgumentException("Unknown type");
        };

        // 2. 공통 필드 설정 (부모 엔티티에 @Setter 필요)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        announcement.setAdmin(admin);
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setOrganizer(dto.getOrganizer());
        announcement.setRecruitmentStart(LocalDateTime.parse(dto.getRecruitmentStart(), formatter));
        // ... 나머지 날짜 및 공통 필드 세팅

        return announcementRepository.save(announcement).getId();
    }
}