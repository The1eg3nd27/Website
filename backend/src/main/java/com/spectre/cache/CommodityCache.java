package com.spectre.cache;

import com.spectre.payload.tools.CommodityResponseDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class CommodityCache {

    private final Map<String, CommodityResponseDto> cache = new HashMap<>();
    private Instant lastUpdated;

    public void updateCache(Map<String, CommodityResponseDto> newData) {
        cache.clear();
        cache.putAll(newData);
        lastUpdated = Instant.now();
    }
    

    public CommodityResponseDto getByName(String name) {
        return cache.get(normalize(name));
    }

    public Integer getIdByName(String name) {
        CommodityResponseDto dto = getByName(name);
        if (dto == null || dto.getId() == null) return null;
        try {
            return Integer.parseInt(dto.getId());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Collection<CommodityResponseDto> getAll() {
        return cache.values();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public int size() {
        return cache.size();
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    private String normalize(String input) {
        return input != null ? input.toLowerCase().trim() : "";
    }
}
