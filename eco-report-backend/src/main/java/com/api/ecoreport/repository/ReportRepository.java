package com.api.ecoreport.repository;

import com.api.ecoreport.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
