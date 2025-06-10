package com.spectre.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;

import com.spectre.payload.tools.CommodityResponseDto;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class CommodityCache {
    private final Map<String, Object> commodities = new ConcurrentHashMap<>();

    public void update(String name, Object data) {
        commodities.put(name.toLowerCase(), data);
    }

    public Object get(String name) {
        return commodities.get(name.toLowerCase());
    }

    public boolean contains(String name) {
        return commodities.containsKey(name.toLowerCase());
    }
    public void updateCache(Map<String, CommodityResponseDto> newCache) {
        commodities.clear();
        commodities.putAll(newCache);
    }      
    public Collection<CommodityResponseDto> getAll() {
        return commodities.values().stream()
                .filter(CommodityResponseDto.class::isInstance)
                .map(CommodityResponseDto.class::cast)
                .toList();
    }

    public CommodityResponseDto getByName(String name) {
        if (name == null) {
            return null;
        }
        Object result = commodities.get(name.toLowerCase());
        return (result instanceof CommodityResponseDto dto) ? dto : null;
    }
    
}
