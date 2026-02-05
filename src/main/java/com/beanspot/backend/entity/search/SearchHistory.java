package com.beanspot.backend.entity.search;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "keyword"})},
        indexes = {@Index(name = "idx_user_updated", columnList = "user_id, updated_at")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static SearchHistory create(Long userId, String keyword) {
        LocalDateTime now = LocalDateTime.now();
        SearchHistory h = new SearchHistory();
        h.userId = userId;
        h.keyword = keyword;
        h.createdAt = now;
        h.updatedAt = now;
        return h;
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
