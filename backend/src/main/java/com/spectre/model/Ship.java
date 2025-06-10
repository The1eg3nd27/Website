package com.spectre.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "ships")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String name;
    private String manufacturer;
    private String type;
    private String focus;
    private String size;
    private Integer crewMin;
    private Integer crewMax;
    private Float length;
    private Float beam;
    private Float height;
    private Float mass;
    private Float cargoCapacity;
    private Integer hp;
    private Integer shieldHp;
    private Integer scmSpeed;
    private Integer navMaxSpeed;
    private Float pitch;
    private Float yaw;
    private Float roll;
    private Float insuranceClaimTime;
    private String productionStatus;
    private String productionNote;
    private Float msrp;

    @Column(length = 1000)
    private String description;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Lob
    private byte[] shipImage;

    private String pledgeUrl;
    private Integer sizeClass;
}
