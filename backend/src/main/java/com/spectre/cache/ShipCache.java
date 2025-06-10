package com.spectre.cache;

import com.spectre.model.Ship;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ShipCache {
    private final List<Ship> ships = new ArrayList<>();

    public void update(List<Ship> newShips) {
        ships.clear();
        ships.addAll(newShips);
    }
}
