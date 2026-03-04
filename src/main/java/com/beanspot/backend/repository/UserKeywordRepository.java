package com.beanspot.backend.repository;

import com.beanspot.backend.entity.notification.UserKeyword;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserKeywordRepository extends CrudRepository<UserKeyword, Long> {
    List<UserKeyword> findAll();
    List<UserKeyword> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndKeyword(Long userId, String keyword);
    Long countByUserId(Long userId);
}
