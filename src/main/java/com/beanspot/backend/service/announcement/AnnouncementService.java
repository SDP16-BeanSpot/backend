package com.beanspot.backend.service.announcement;

import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.announcement.AnnouncementDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSearchConditionDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import org.springframework.web.multipart.MultipartFile;


public interface AnnouncementService {
    PageResponse<AnnouncementSummaryDTO> getAnnouncements(Long userId, AnnouncementSearchConditionDTO condition);

    AnnouncementDTO.Detail getAnnouncementDetail(Long id);

    void increaseViewCount(Long id);

    void addAnnouncement(AnnouncementDTO.Create announcement, MultipartFile image);

    void updateAnnouncement(Long id, AnnouncementDTO.Update announcement);

    void deleteAnnouncement(Long id);
}
