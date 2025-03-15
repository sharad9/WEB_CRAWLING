package org.com.service;

import org.com.entity.CrawlResult;
import org.com.entity.CrawledUrl;
import org.com.entity.CrawlResult.CrawlStatus;
import org.com.repository.CrawlResultRepository;
import org.com.repository.CrawledUrlRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class WebCrawlerService {
    private final CrawlResultRepository resultRepository;
    private final CrawledUrlRepository urlRepository;

    public WebCrawlerService(CrawlResultRepository resultRepository, CrawledUrlRepository urlRepository) {
        this.resultRepository = resultRepository;
        this.urlRepository = urlRepository;
    }

    @Async
    public CompletableFuture<CrawlResult> startCrawl(String url) {
        CrawlResult crawlResult = new CrawlResult();
        crawlResult.setSeedUrl(url);
        crawlResult.setStatus(CrawlStatus.IN_PROGRESS);
        crawlResult.setCreatedAt(LocalDateTime.now());
        crawlResult = resultRepository.save(crawlResult);

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            List<CrawledUrl> crawledUrls = new ArrayList<>();
            for (Element link : links) {
                CrawledUrl crawledUrl = new CrawledUrl(link.absUrl("href"), crawlResult);
                crawledUrls.add(crawledUrl);
            }

            urlRepository.saveAll(crawledUrls);
            crawlResult.setStatus(CrawlStatus.COMPLETED);
        } catch (IOException e) {
            crawlResult.setStatus(CrawlStatus.FAILED);
            e.printStackTrace();
        }

        resultRepository.save(crawlResult);
        return CompletableFuture.completedFuture(crawlResult);
    }

    public Optional<CrawlResult> getCrawlResult(Long id) {
        return resultRepository.findById(id);
    }


    public Page<CrawledUrl> getCrawledUrls(Long crawlResultId, Pageable pageable) {
        return resultRepository.findById(crawlResultId)
                .map(result -> urlRepository.findByCrawlResult(result, pageable))
                .orElse(Page.empty());
    }


    public List<CrawlResult> getAllCrawlResults() {
        return resultRepository.findAll();
    }
}
