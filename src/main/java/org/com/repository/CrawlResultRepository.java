package org.com.repository;

import io.swagger.v3.oas.annotations.Operation;
import org.com.entity.CrawlResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CrawlResultRepository extends JpaRepository<CrawlResult, Long> {
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);


}
