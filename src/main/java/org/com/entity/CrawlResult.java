package org.com.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CrawlResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seedUrl;

    @OneToMany(mappedBy = "crawlResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CrawledUrl> crawledUrls;

    @Enumerated(EnumType.STRING)
    private CrawlStatus status;

    private LocalDateTime createdAt;

    public enum CrawlStatus {
        IN_PROGRESS, COMPLETED, FAILED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public List<CrawledUrl> getCrawledUrls() {
        return crawledUrls;
    }

    public void setCrawledUrls(List<CrawledUrl> crawledUrls) {
        this.crawledUrls = crawledUrls;
    }

    public CrawlStatus getStatus() {
        return status;
    }

    public void setStatus(CrawlStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
