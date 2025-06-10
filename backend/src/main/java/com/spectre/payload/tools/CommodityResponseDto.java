package com.spectre.payload.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommodityResponseDto {
    private String id;
    private String commodityName;
    private String bestBuyLocation;
    private double bestBuyPrice;
    private String bestSellLocation;
    private double bestSellPrice;
    private double profitPerUnit;
    private double potentialProfit;
    private String recommendation;
    private String fallbackInfo;
}

