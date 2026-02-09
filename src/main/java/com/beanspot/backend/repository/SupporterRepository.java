package com.beanspot.backend.repository;

import com.beanspot.backend.entity.AnnouncementSupporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupporterRepository extends JpaRepository<AnnouncementSupporter, Long> {
}