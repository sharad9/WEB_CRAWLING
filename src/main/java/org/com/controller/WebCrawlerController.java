package org.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.com.entity.CrawlResult;
import org.com.entity.CrawledUrl;
import org.com.service.WebCrawlerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/crawl")
@Tag(name = "Web Crawler API", description = "APIs for crawling web pages")
public class WebCrawlerController {
    private final WebCrawlerService crawlerService;

    public WebCrawlerController(WebCrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping
    @Operation(summary = "Start Crawl Job", description = "Starts a new web crawl job with the given URL.")
    public ResponseEntity<String> startCrawl(@RequestParam String url) {
        CompletableFuture<CrawlResult> future = crawlerService.startCrawl(url);
        return ResponseEntity.accepted().body("Crawl started. Check status with GET /crawl/{id}");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Crawl Status", description = "Fetches the status and result of a specific crawl job by ID.")
    public ResponseEntity<CrawlResult> getCrawlResult(@PathVariable Long id) {
        Optional<CrawlResult> result = crawlerService.getCrawlResult(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("fetch-crawled-result-list")
    @Operation(summary = "Get All Crawl Results", description = "Fetches all crawl results.")
    public ResponseEntity<List<CrawlResult>> getAllCrawlResults() {
        List<CrawlResult> results = crawlerService.getAllCrawlResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}/paginated-crawled-result")
    @Operation(summary = "Get Paginated Crawled Result By Id", description = "Fetches paginated result of a specific crawl job by ID.")
    public ResponseEntity<Page<CrawledUrl>> getCrawledUrls(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sortParam) {

        // Split the sort parameter (default "id,asc")
        String[] sortParts = sortParam.split(",");

        // Default sort column and direction
        String sortField = sortParts[0];
        String sortDirection = (sortParts.length > 1) ? sortParts[1] : "asc";

        // Create Sort object safely
        Sort sorting = Sort.by(Sort.Order.by(sortField).with(Sort.Direction.fromString(sortDirection)));
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<CrawledUrl> urls = crawlerService.getCrawledUrls(id, pageable);
        return ResponseEntity.ok(urls);
    }




}
