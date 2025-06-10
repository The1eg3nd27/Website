package com.spectre.payload.tools;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter
@Setter
@Data
public class CommodityRequestDto {
    private String currentSystem;
    private String currentLocation;
    private String commodity;
    private int quantity;
    private boolean allowSystemChange;
}
