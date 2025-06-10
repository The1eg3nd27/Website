package com.spectre.payload.tools;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipComparisonResultDto {
    private ShipStats ship1;
    private ShipStats ship2;

    @Data
    @Builder
    public static class ShipStats {
        private String name;
        private String manufacturer;
        private String type;
        private String focus;

        private Float length;
        private Float beam;
        private Float height;

        private Integer sizeClass;
        private String size;

        private Integer crewMin;
        private Integer crewMax;

        private Float mass;
        private Float cargo;

        private Integer hp;
        private Integer scmSpeed;
        private Integer maxSpeed;

        private Float pitch;
        private Float yaw;
        private Float roll;

        private Float insuranceClaimTime;

        private String productionStatus;
        private String productionNote;

        private Double msrp;
        private String pledgeUrl;

        private String description;
        private byte[] image;
    }
}
