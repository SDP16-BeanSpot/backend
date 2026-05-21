package com.beanspot.backend.repository;

import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import com.beanspot.backend.entity.Bookmark;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.announcement.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserAndAnnouncement(User user, Announcement announcement);
    Optional<Bookmark> findByUserIdAndAnnouncementId(Long userId, Long announcementId);
    List<Bookmark> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    List<Bookmark> findAllByAnnouncement(Announcement announcement);
    @Query("""
    select b.announcement.id
    from Bookmark b
    where b.user.id = :userId
    and b.announcement.id in :ids
    """)
    List<Long> findBookmarkedAnnouncementIds(Long userId, List<Long> ids);

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
            true
        )
        from Bookmark b
        join b.announcement a
        where b.user.id = :userId
        order by b.createdAt desc
    """)
    List<AnnouncementSummaryDTO> findBookmarkedAnnouncements(Long userId);
}
