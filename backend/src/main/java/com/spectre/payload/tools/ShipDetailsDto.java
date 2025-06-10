package com.spectre.payload.tools;

import com.spectre.model.Ship;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipDetailsDto {
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
    private Integer navMaxSpeed;

    private Float pitch;
    private Float yaw;
    private Float roll;

    private Float insuranceClaimTime;

    private String productionStatus;
    private String productionNote;

    private Float msrp;
    private String pledgeUrl;

    private String description;
    private byte[] image;
    private String uuid;
    private Float cargoCapacity;
    private Integer shieldHp;

    public static ShipDetailsDto fromEntity(Ship ship) {
    return ShipDetailsDto.builder()
        .name(ship.getName())
        .manufacturer(ship.getManufacturer())
        .type(ship.getType())
        .focus(ship.getFocus())
        .length(ship.getLength())
        .beam(ship.getBeam())
        .height(ship.getHeight())
        .sizeClass(ship.getSizeClass())
        .size(ship.getSize())
        .crewMin(ship.getCrewMin())
        .crewMax(ship.getCrewMax())
        .mass(ship.getMass())
        .cargo(ship.getCargoCapacity())
        .hp(ship.getHp())
        .scmSpeed(ship.getScmSpeed())
        .navMaxSpeed(ship.getNavMaxSpeed())
        .pitch(ship.getPitch())
        .yaw(ship.getYaw())
        .roll(ship.getRoll())
        .insuranceClaimTime(ship.getInsuranceClaimTime())
        .productionStatus(ship.getProductionStatus())
        .productionNote(ship.getProductionNote())
        .msrp(ship.getMsrp())
        .pledgeUrl(ship.getPledgeUrl())
        .description(ship.getDescription())
        .image(ship.getShipImage())
        .uuid(ship.getUuid())
        .cargoCapacity(ship.getCargoCapacity())
        .shieldHp(ship.getShieldHp())
        .build();
}


}

