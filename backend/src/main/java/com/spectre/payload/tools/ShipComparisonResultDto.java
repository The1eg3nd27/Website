package com.spectre.payload.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor 
@NoArgsConstructor 
public class ShipComparisonResultDto {

    private ShipStats shipA;
    private ShipStats shipB;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipStats {
        private String name;
        private String manufacturer;
        private String type;
        private String focus;
        private String size;
        private int crewMin;
        private int crewMax;
        private float length;
        private float beam;
        private float height;
        private float mass;
        private float cargoCapacity;
        private int hp;
        private int shieldHp;
        private int scmSpeed;
        private int navMaxSpeed;
        private float pitch;
        private float yaw;
        private float roll;
        private float insuranceClaimTime;
        private String productionStatus;
        private String productionNote;
        private double msrp;
        private String description;
        private String pledgeUrl;
        private int sizeClass;
    }
}
