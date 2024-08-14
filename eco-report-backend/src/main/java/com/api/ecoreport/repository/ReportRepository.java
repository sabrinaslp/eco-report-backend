package com.api.ecoreport.repository;

import com.api.ecoreport.model.Report;
import com.api.ecoreport.model.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserId(Long userId);
    List<Report> findByStatus(ReportStatus status);
}
