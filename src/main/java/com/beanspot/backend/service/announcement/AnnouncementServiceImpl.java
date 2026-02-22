package com.beanspot.backend.service.announcement;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.announcement.*;
import com.beanspot.backend.entity.announcement.*;
import com.beanspot.backend.listener.AnnouncementCreatedEvent;
import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import com.beanspot.backend.repository.announcement.AnnouncementScheduleRepository;
import com.beanspot.backend.service.KakaoGeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementSearchService searchService;
    private final KakaoGeoCodingService geoCodingService;

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementScheduleRepository scheduleRepository;
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
                .orElseThrow(() -> new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        return  AnnouncementDTO.Detail.from(announcement);

    }

    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        announcement.setViewCount(announcement.getViewCount() + 1);
    }

    @Override
    @Transactional
    public void addAnnouncement(AnnouncementDTO.Create reqDTO) {
        GeoPoint geo = geoCodingService.convert(reqDTO.getLocation());
        Double lat = (geo != null) ? geo.getLat() : null;
        Double lng = (geo != null) ? geo.getLon() : null;

        // 공고 저장
        Announcement announcement = Announcement.builder()
                .title(reqDTO.getTitle())
                .type(reqDTO.getType())
                .activityContent(reqDTO.getActivityContent())
                .detailContent(reqDTO.getDetailContent())
                .organizer(reqDTO.getOrganizer())
                .organizerImgUrl(reqDTO.getOrganizerImgUrl())
                .imgUrl(reqDTO.getImgUrl())
                .region(reqDTO.getRegion())
                .location(reqDTO.getLocation())
                .startDate(reqDTO.getStartDate())
                .endDate(reqDTO.getEndDate())
                .recruitmentStart(reqDTO.getRecruitmentStart())
                .recruitmentEnd(reqDTO.getRecruitmentEnd())
                .fee(reqDTO.getFee())
                .lat(lat)
                .lng(lng)
                .serviceHoursVerified(reqDTO.getServiceHoursVerified())
                .selectionProcess(reqDTO.getSelectionProcess())
                .awardScale(reqDTO.getAwardScale())
                .activityMethod(reqDTO.getActivityMethod())
                .applyMethod(reqDTO.getApplyMethod())
                .benefits(reqDTO.getBenefits())
                .build();

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        if (reqDTO.getSchedules() != null) {
            List<AnnouncementDTO.Create.ScheduleRequestDTO> scheduleDtos = reqDTO.getSchedules();
            List<AnnouncementSchedule> schedules = IntStream.range(0, scheduleDtos.size())
                    .mapToObj(i -> {
                        AnnouncementDTO.Create.ScheduleRequestDTO sDto = scheduleDtos.get(i);
                        return AnnouncementSchedule.builder()
                                .announcement(savedAnnouncement)
                                .scheduleDate(sDto.getScheduleDate())
                                .content(sDto.getContent())
                                .sequenceOrder(i + 1) // 리스트의 순서(index)를 그대로 순번으로 사용
                                .build();
                    })
                    .toList();
            scheduleRepository.saveAll(schedules);
        }

        applicationEventPublisher.publishEvent(
                new AnnouncementCreatedEvent(announcement.getId())
        );
    }

    @Override
    @Transactional
    public void updateAnnouncement(Long id, AnnouncementDTO.Update reqDTO) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));

        announcement.setTitle(reqDTO.getTitle());
        announcement.setStartDate(reqDTO.getStartDate());
        announcement.setEndDate(reqDTO.getEndDate());
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND);
        }
        announcementRepository.deleteById(id);
    }

}
