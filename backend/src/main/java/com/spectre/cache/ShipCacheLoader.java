package com.spectre.cache;

import com.spectre.repository.ShipRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipCacheLoader {

    private final ShipRepository shipRepository;
    private final ShipCache shipCache;

    @PostConstruct
    public void loadCacheOnStartup() {
        refresh();
    }

    @Scheduled(cron = "0 0 3 * * *") 
    public void refresh() {
        shipCache.update(shipRepository.findAll());
        System.out.println("🔁 ShipCache refreshed with " + shipCache.getShips().size() + " ships.");
    }
}
