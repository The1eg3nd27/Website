package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeRouteResponseDto {

    private String bestBuyLocation;
    private double bestBuyPrice;

    private String bestSellLocation;
    private double sellPrice;

    private String system;
    private String riskLevel;

    private double expectedProfit;
    private String recommendation;
}
