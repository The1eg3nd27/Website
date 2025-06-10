package com.spectre.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    private static final long LIMIT_INTERVAL_MS = 10_000; // 10s
    private static final int MAX_REQUESTS = 20;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        RequestCounter counter = requestCounts.computeIfAbsent(ip, k -> new RequestCounter());

        synchronized (counter) {
            long now = Instant.now().toEpochMilli();
            if (now - counter.timestamp > LIMIT_INTERVAL_MS) {
                counter.timestamp = now;
                counter.count = 0;
            }

            counter.count++;
            if (counter.count > MAX_REQUESTS) {
                response.setStatus(429);
                response.getWriter().write("Too many requests");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    static class RequestCounter {
        int count = 0;
        long timestamp = Instant.now().toEpochMilli();
    }
}
