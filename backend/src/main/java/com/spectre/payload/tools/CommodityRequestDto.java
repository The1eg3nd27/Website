package com.spectre.payload.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommodityRequestDto {

    private String commodityName;
    private int quantity;

    public String getCommodityName() {
        return commodityName != null ? commodityName.trim() : null;
    }

    public int getQuantity() {
        return Math.max(quantity, 1);
    }
}
