package com.spectre.payload.tools;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ShipCompareRequestDto {
    private String shipA;
    private String shipB;
}
