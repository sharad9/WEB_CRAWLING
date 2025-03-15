package org.com.repository;

import org.com.entity.CrawledUrl;
import org.com.entity.CrawlResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawledUrlRepository extends JpaRepository<CrawledUrl, Long> {
    Page<CrawledUrl> findByCrawlResult(CrawlResult crawlResult, Pageable pageable);
}
