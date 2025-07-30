package org.utarid.resilience;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.utarid.resilience.exception.ResilienceException;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ResilienceService {

    static final String SUCCESS = "success";
    private final Random random = new Random();

    @Retry(name = "resilienceServiceCB", fallbackMethod = "retryFallback")
    public String testRetry() {
        if (random.nextDouble() < 0.9) {
            throw new ResilienceException("Service temporarily unavailable");
        }
        return SUCCESS;
    }

    @CircuitBreaker(name = "resilienceServiceCB", fallbackMethod = "circuitBreakerFallback")
    public String testCB() {
        if (random.nextDouble() < 0.9) {
            throw new ResilienceException("Circuit breaker test failure");
        }
        return SUCCESS;
    }

    @TimeLimiter(name = "resilienceServiceCB", fallbackMethod = "timeoutFallback")
    public CompletableFuture<String> testTimeLimiter() {
        int delay = random.nextInt(2000);
        return CompletableFuture.supplyAsync(
                () -> "Completed in " + delay + "ms",
                CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
        );
    }

    @RateLimiter(name = "resilienceServiceCB", fallbackMethod = "rateLimiterFallback")
    public String testRateLimiter() {
        return SUCCESS;
    }

    @Bulkhead(name = "resilienceServiceCB", fallbackMethod = "bulkheadFallback")
    public String testBulkhead() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResilienceException("Bulkhead operation interrupted", e);
        }
        return SUCCESS;
    }

    public String bulkheadFallback(BulkheadFullException ex) {
        return "Too many concurrent requests: " + ex;
    }

    public String rateLimiterFallback(RequestNotPermitted ex) {
        return "too many requests: " + ex;
    }

    public CompletableFuture<String> timeoutFallback(Throwable t) {
        return CompletableFuture.completedFuture("timeout fallback method was called: " + t);
    }

    public String retryFallback(Throwable t) {
        return "retry fallback method was called: " + t;
    }

    public String circuitBreakerFallback(Throwable t) {
        return "circuit breaker fallback method was called: " + t.getMessage();
    }

}
