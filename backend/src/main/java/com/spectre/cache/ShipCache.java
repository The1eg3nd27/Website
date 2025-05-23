package com.spectre.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShipCache {

    private List<String> shipNames = new ArrayList<>();

    public void updateNames(List<String> names) {
        this.shipNames = names;
    }

    public List<String> getAllNames() {
        return shipNames;
    }
}
