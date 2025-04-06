package com.ced.security;

import com.github.benmanes.caffeine.cache.Cache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RateLimitFilter Tests")
class RateLimitFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Cache<String, Bucket> bucketCache;
    
    @Mock
    private Bucket bucket;
    
    @Mock
    private ConsumptionProbe probe;
    
    @Mock
    private PrintWriter writer;
    
    @Captor
    private ArgumentCaptor<Function<String, Bucket>> bucketCreatorCaptor;

    private Bandwidth limit;
    private RateLimitFilter rateLimitFilter;

    @BeforeEach
    void setUp() {
        Refill refill = Refill.greedy(1, Duration.ofSeconds(1));
        limit = Bandwidth.classic(5, refill);
        
        rateLimitFilter = new RateLimitFilter(bucketCache, limit);
    }

    @Test
    @DisplayName("Should allow request when under rate limit")
    void shouldAllowRequestWhenUnderRateLimit() throws ServletException, IOException {
        // AAA - Arrange
        String clientIp = "127.0.0.1";
        when(request.getRemoteAddr()).thenReturn(clientIp);
        
        when(bucketCache.get(eq(clientIp), any())).thenReturn(bucket);
        when(bucket.tryConsumeAndReturnRemaining(1)).thenReturn(probe);
        when(probe.isConsumed()).thenReturn(true);

        // AAA - Act
        rateLimitFilter.doFilterInternal(request, response, filterChain);

        // AAA - Assert
        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }

    @Test
    @DisplayName("Should block request when rate limit exceeded")
    void shouldBlockRequestWhenRateLimitExceeded() throws ServletException, IOException {
        // AAA - Arrange
        String clientIp = "127.0.0.1";
        when(request.getRemoteAddr()).thenReturn(clientIp);
        
        when(bucketCache.get(eq(clientIp), any())).thenReturn(bucket);
        when(bucket.tryConsumeAndReturnRemaining(1)).thenReturn(probe);
        when(probe.isConsumed()).thenReturn(false);
        when(response.getWriter()).thenReturn(writer);

        // AAA - Act
        rateLimitFilter.doFilterInternal(request, response, filterChain);

        // AAA - Assert
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(writer).write(anyString());
    }

    @Test
    @DisplayName("Should create new bucket for new client")
    void shouldCreateNewBucketForNewClient() throws ServletException, IOException {
        // AAA - Arrange
        String clientIp = "192.168.1.1";
        when(request.getRemoteAddr()).thenReturn(clientIp);
        
        when(bucketCache.get(eq(clientIp), bucketCreatorCaptor.capture())).thenReturn(bucket);
        when(bucket.tryConsumeAndReturnRemaining(1)).thenReturn(probe);
        when(probe.isConsumed()).thenReturn(true);

        // AAA - Act
        rateLimitFilter.doFilterInternal(request, response, filterChain);

        // AAA - Assert - Verify the bucket creator function was provided
        verify(filterChain).doFilter(request, response);
        Function<String, Bucket> bucketCreator = bucketCreatorCaptor.getValue();
    }
} 