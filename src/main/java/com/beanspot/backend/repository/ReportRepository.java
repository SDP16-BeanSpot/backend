package com.beanspot.backend.repository;

import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value = "SELECT r FROM Report r JOIN FETCH r.reporter JOIN FETCH r.reportedUser JOIN FETCH r.message",
           countQuery = "SELECT COUNT(r) FROM Report r")
    Page<Report> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT r FROM Report r JOIN FETCH r.reporter JOIN FETCH r.reportedUser JOIN FETCH r.message WHERE r.status = :status",
           countQuery = "SELECT COUNT(r) FROM Report r WHERE r.status = :status")
    Page<Report> findAllByStatusWithDetails(@Param("status") ReportStatus status, Pageable pageable);

    @Query("SELECT r FROM Report r JOIN FETCH r.reporter JOIN FETCH r.reportedUser JOIN FETCH r.message WHERE r.id = :reportId")
    Optional<Report> findByIdWithDetails(@Param("reportId") Long reportId);

    boolean existsByReporter_IdAndMessage_Id(Long reporterId, Long messageId);
}
