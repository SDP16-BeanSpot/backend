package com.beanspot.backend.repository;

import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import com.beanspot.backend.entity.RecentAnnouncementView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecentAnnouncementViewRepository extends CrudRepository<RecentAnnouncementView, Long> {
    long countByUserId(Long userId);
    List<RecentAnnouncementView> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
    Optional<RecentAnnouncementView> findByUserIdAndAnnouncementId(Long userId, Long announcementId);
    Optional<RecentAnnouncementView> findFirstByUserIdOrderByUpdatedAtAsc(Long userId);


    @Query("""
        select new com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO(
            a.id,
            a.title,
            a.region,
            a.activityMethod,
            a.type,
            a.startDate,
            a.endDate,
            a.recruitmentEnd,
            a.imgUrl,
            case when b.id is not null then true else false end
        )
        from RecentAnnouncementView rav
        join rav.announcement a
        left join Bookmark b
            on b.announcement.id = a.id
           and b.user.id = :userId
        where rav.user.id = :userId
        order by rav.updatedAt desc
    """)
    List<AnnouncementSummaryDTO> findRecentAnnouncements(Long userId);
}
