package org.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class CrawledUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", length = 2083) 
    private String url;

    @ManyToOne
    @JoinColumn(name = "crawl_result_id")
    @JsonIgnore
    private CrawlResult crawlResult;

    public CrawledUrl() {}

    public CrawledUrl(String url, CrawlResult crawlResult) {
        this.url = url;
        this.crawlResult = crawlResult;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public CrawlResult getCrawlResult() { return crawlResult; }
    public void setCrawlResult(CrawlResult crawlResult) { this.crawlResult = crawlResult; }
}
