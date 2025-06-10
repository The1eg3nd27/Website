package com.spectre.cache;

import com.spectre.model.PriceEntry;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommodityPriceCache {

    private final Map<String, List<PriceEntry>> priceByCommodityId = new ConcurrentHashMap<>();

    private final Map<String, String> nameToIdMap = new ConcurrentHashMap<>();

    private final Map<String, List<PriceEntry>> pricesByCommodityId = new HashMap<>();
    
    private List<PriceEntry> entries = new ArrayList<>();


    public void updateCache(List<PriceEntry> entries) {
        priceByCommodityId.clear();
        nameToIdMap.clear();
    
        for (PriceEntry entry : entries) {
            if (entry.getCommodityId() == null || entry.getCommodityName() == null) continue;
    
            priceByCommodityId
                .computeIfAbsent(entry.getCommodityId(), k -> new ArrayList<>())
                .add(entry);
    
            nameToIdMap.putIfAbsent(entry.getCommodityName().toLowerCase(), entry.getCommodityId());
            
        }
    }
    

    public List<PriceEntry> getPricesForCommodity(String id) {
        return priceByCommodityId.getOrDefault(id, Collections.emptyList()); // ✅ correct map
    }

    public Optional<String> resolveIdByName(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(nameToIdMap.get(name.toLowerCase()));
    }

    public Set<String> getAllCommodityNames() {
        return nameToIdMap.keySet();
    }

    public Collection<List<PriceEntry>> getAllPriceEntries() {
        return priceByCommodityId.values();
    }
    public List<PriceEntry> getAllPrices() {
        return this.entries;
    }
    
}
