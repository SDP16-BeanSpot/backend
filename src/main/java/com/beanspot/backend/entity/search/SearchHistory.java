package com.beanspot.backend.entity.search;

import com.beanspot.backend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "search_history",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "keyword"})},
        indexes = {@Index(name = "idx_user_updated", columnList = "user_id, updated_at")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String keyword;

    public static SearchHistory create(Long userId, String keyword) {
        SearchHistory h = new SearchHistory();
        h.userId = userId;
        h.keyword = keyword;
        return h;
    }

}
