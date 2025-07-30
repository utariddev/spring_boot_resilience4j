package org.utarid.resilience;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class ResilienceController {
    private final ResilienceService resilienceService;

    public ResilienceController(ResilienceService resilienceService) {
        this.resilienceService = resilienceService;
    }

    @GetMapping("/resilience/cb")
    public String testCB() {
        return resilienceService.testCB();
    }

    @GetMapping("/resilience/retry")
    public String testRetry() {
        return resilienceService.testRetry();
    }

    @GetMapping("/resilience/timelimiter")
    public CompletableFuture<String> testTimeLimiter() {
        return resilienceService.testTimeLimiter();
    }

    @GetMapping("/resilience/ratelimiter")
    public String testRateLimiter() {
        return resilienceService.testRateLimiter();
    }

    @GetMapping("/resilience/bulkhead")
    public String testBulkhead() {
        return resilienceService.testBulkhead();
    }

}