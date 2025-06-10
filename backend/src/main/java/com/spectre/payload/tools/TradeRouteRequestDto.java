package com.spectre.payload.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeRouteRequestDto {
    private String commodityName;
    private int quantity;
    private String currentSystem;
    private boolean canTravelSystems;
}
