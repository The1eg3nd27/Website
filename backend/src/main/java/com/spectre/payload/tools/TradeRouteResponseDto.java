package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TradeRouteResponseDto {
    private String fromLocation;
    private String toLocation;
    private String commodity;
    private double buyPrice;
    private double sellPrice;
    private int quantity;
    private double totalProfit;
    private boolean crossSystem;
    private String message;
}
