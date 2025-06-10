package com.spectre.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntry {

    private String commodityId;
    private String commodityName;

    private String location;
    private String terminalName;
    private String terminal;

    private Double priceBuy;
    private Double priceSell;

    private boolean isAvailable;
    private boolean isAvailableLive;

    private boolean isBuyable;
    private boolean isSellable;

    private boolean isIllegal;
    private boolean isVolatile;

    private Double weightScu;

    private String locationCode;
    private String commodityCode;

    private boolean isRefined;
    private boolean isRaw;
    private boolean isHarvestable;
}
