package com.spectre.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommodityCacheRefresher {

    private final CommodityCache commodityCache;

    @Scheduled(cron = "0 15 3 * * *") 
    public void refresh() {
        System.out.println("🔁 CommodityCache refresh triggered (implement logic here)");
    }
    
}
