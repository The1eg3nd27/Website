package com.spectre.payload.tools;

import lombok.Data;

import java.time.Instant;

@Data
public class EarningsHistoryDto {
    private double totalProfit;
    private double profitPerPlayer;
    private boolean profit;
    private String note;
    private Instant timestamp;
    
    public boolean isProfit() {
        return profit;
    }
    
    public void setProfit(boolean profit) {
        this.profit = profit;
    }
}
