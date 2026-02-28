package com.beanspot.backend.repository;

import com.beanspot.backend.entity.search.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    Optional<SearchHistory> findByUserIdAndKeyword(Long userId, String keyword);

    List<SearchHistory> findTop5ByUserIdOrderByUpdatedAtDesc(Long userId);

    void deleteByUserId(Long userId);

    @Query(value = """
    DELETE FROM search_history
    WHERE user_id = :user_id
    AND id NOT IN (
        SELECT id        
        FROM user_id = :userId
        ORDER BY updated_at DESC            
        LIMIT :limit
    )
    """, nativeQuery = true)
    void deleteOverflow(@Param("userId") Long user_id, @Param("limit") int limit);
}
