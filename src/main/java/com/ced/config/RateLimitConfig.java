package com.ced.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RateLimitConfig {

    @Value("${rate-limit.cache.expire-after-access}")
    private String cacheExpireAfterAccess;

    @Value("${rate-limit.cache.maximum-size}")
    private int cacheMaximumSize;

    @Value("${rate-limit.bandwidth.capacity}")
    private int bandwidthCapacity;

    @Value("${rate-limit.bandwidth.refill-interval}")
    private String refillInterval;

    @Value("${rate-limit.bandwidth.refill-tokens}")
    private int refillTokens;

    @Bean("bucketCache")
    public Cache<String, io.github.bucket4j.Bucket> bucketCache() {
        long expireDuration = Duration.parse("PT" + cacheExpireAfterAccess.toUpperCase()).toHours();
        return Caffeine.newBuilder()
                .expireAfterAccess(expireDuration, TimeUnit.HOURS)
                .maximumSize(cacheMaximumSize)
                .build();
    }

    @Bean
    public Bandwidth limit() {
        Duration refillDuration = Duration.parse("PT" + refillInterval.toUpperCase());
        Refill refill = Refill.greedy(refillTokens, refillDuration);
        return Bandwidth.classic(bandwidthCapacity, refill);
    }
}
