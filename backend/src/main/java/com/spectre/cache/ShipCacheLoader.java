package com.spectre.cache;

import com.spectre.model.Ship;
import com.spectre.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ShipCacheLoader {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ShipCache shipCache;

    @PostConstruct
    @Scheduled(cron = "0 0 8 * * *") 
    public void loadShipNames() {
        System.out.println("ShipCacheLoader triggered (startup or scheduled)");

        List<String> names = shipRepository.findAll().stream()
                .map(Ship::getName)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        shipCache.updateNames(names);
        System.out.println("Loaded " + names.size() + " ship names into cache.");
    }
}
