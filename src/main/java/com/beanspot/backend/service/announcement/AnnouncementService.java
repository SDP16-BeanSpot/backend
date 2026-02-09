package com.beanspot.backend.service.announcement;

import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.announcement.AnnouncementDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSearchConditionDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;


public interface AnnouncementService {
    PageResponse<AnnouncementSummaryDTO> getAnnouncements(AnnouncementSearchConditionDTO condition);

    AnnouncementDTO.Detail getAnnouncementDetail(Long id);

    void increaseViewCount(Long id);

    void addAnnouncement(AnnouncementDTO.Create announcement);

    void updateAnnouncement(Long id, AnnouncementDTO.Update announcement);

    void deleteAnnouncement(Long id);
}
