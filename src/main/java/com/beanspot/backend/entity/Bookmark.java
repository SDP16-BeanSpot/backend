package com.beanspot.backend.entity;

import com.beanspot.backend.entity.announcement.Announcement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "bookmark", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "announcement_id"})
})
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;

}
