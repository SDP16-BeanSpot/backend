package com.beanspot.backend.repository;

import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByStatusOrderByCreatedAtDesc(ReportStatus status);
    boolean existsByReporter_IdAndMessage_Id(Long reporterId, Long messageId);
}
