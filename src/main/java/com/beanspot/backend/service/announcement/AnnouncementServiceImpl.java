package com.beanspot.backend.service.announcement;

import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.announcement.*;
import com.beanspot.backend.entity.announcement.*;
import com.beanspot.backend.listener.AnnouncementCreatedEvent;
import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import com.beanspot.backend.service.KakaoGeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementSearchService searchService;
    private final KakaoGeoCodingService geoCodingService;

    private final AnnouncementRepository announcementRepository;
   private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AnnouncementSummaryDTO> getAnnouncements(
            AnnouncementSearchConditionDTO condition
    ) {
        return searchService.search(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementDTO.Detail getAnnouncementDetail(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No announcement found with id: " + id));

        return  AnnouncementDTO.Detail.from(announcement);

    }

    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No announcement found with id: " + id));

        announcement.setViewCount(announcement.getViewCount() + 1);
    }

    @Override
    @Transactional
    public void addAnnouncement(AnnouncementDTO.Create reqDTO) {

        validateType(reqDTO);


        Double lat = null;
        Double lng = null;

        try {
            GeoPoint geo = geoCodingService.convert(reqDTO.getLocation());
            lat = geo.getLat();
            lng = geo.getLon();
        } catch (Exception e) {
            throw new IllegalArgumentException("위치 정보를 찾을 수 없습니다.");
        }

        // 공고 저장
        Announcement announcement = Announcement.builder()
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .type(reqDTO.getType())
                .organizer(reqDTO.getOrganizer())
                .imgUrl(reqDTO.getImgUrl())
                .region(reqDTO.getRegion())
                .location(reqDTO.getLocation())
                .startDate(reqDTO.getStartDate())
                .endDate(reqDTO.getEndDate())
                .recruitmentStart(reqDTO.getRecruitmentStart())
                .recruitmentEnd(reqDTO.getRecruitmentEnd())
                .fee(reqDTO.getFee())
                .scheduleDetail(reqDTO.getScheduleDetail())
                .lat(lat)
                .lng(lng)
                .serviceHoursVerified(reqDTO.getServiceHoursVerified())
                .selectionProcess(reqDTO.getSelectionProcess())
                .awardScale(reqDTO.getAwardScale())
                .build();

        announcementRepository.save(announcement);

        applicationEventPublisher.publishEvent(
                new AnnouncementCreatedEvent(announcement.getId())
        );
    }

    @Override
    @Transactional
    public void updateAnnouncement(Long id, AnnouncementDTO.Update reqDTO) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("공고가 존재하지 않습니다."));

        announcement.setTitle(reqDTO.getTitle());
        announcement.setStartDate(reqDTO.getStartDate());
        announcement.setEndDate(reqDTO.getEndDate());
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new IllegalArgumentException("공고가 존재하지 않습니다.");
        }
        announcementRepository.deleteById(id);
    }


    private void validateType(AnnouncementDTO.Create reqDTO) {
        switch (reqDTO.getType()) {
            case VOLUNTEER -> {
                if (reqDTO.getServiceHoursVerified() == null) {
                    throw new IllegalArgumentException("봉사시간 인정 여부는 필수입니다.");
                }
            }
            case SUPPORTER -> {
                if (reqDTO.getSelectionProcess() == null || reqDTO.getSelectionProcess().isBlank()) {
                    throw new IllegalArgumentException("서포터즈는 심사 방식이 필요합니다.");
                }
            }
            case CHALLENGE -> {
                if (reqDTO.getTeamSize() == null || reqDTO.getTeamSize().isBlank()) {
                    throw new IllegalArgumentException("챌린지는 팀 규모가 필요합니다.");
                }
            }
            case EDUCATION -> {
                if (reqDTO.getFee() == null) {
                    throw new IllegalArgumentException("교육 프로그램은 비용 정보가 필요합니다.");
                }
            }
        }
    }
}
