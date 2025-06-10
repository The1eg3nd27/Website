package com.spectre.payload.tools;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@Data
public class CommodityRequestDto {
    private String currentSystem;
    private String currentLocation;
    @JsonProperty("commodity")
    @JsonAlias({"commodityName"})
    private String commodity;
    private int quantity;
    private boolean allowSystemChange;
}
