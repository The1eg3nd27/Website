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

    private static final int MAX_REQUESTS_PER_MINUTE = 100; //anpassen bei deployment
    private static final long TIME_WINDOW_MILLIS = 60_000;

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIP = getClientIP(request);
        long currentTime = Instant.now().toEpochMilli();

        RequestCounter counter = requestCounts.computeIfAbsent(clientIP, ip -> new RequestCounter());
        synchronized (counter) {
            if (currentTime - counter.startTime > TIME_WINDOW_MILLIS) {
                counter.count = 1;
                counter.startTime = currentTime;
            } else {
                counter.count++;
            }

            if (counter.count > MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(429); //zu viele anfragen
                response.getWriter().write("Too many requests - please wait and try again.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader == null) ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }

    private static class RequestCounter {
        long startTime = Instant.now().toEpochMilli();
        int count = 0;
    }
}
