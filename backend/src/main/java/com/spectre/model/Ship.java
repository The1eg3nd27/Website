package com.spectre.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
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
    
    @Column(name = "description", length = 1000)
    private String description;

    private Float length;
    private Float beam;
    private Float height;

    @Column(name = "size_class")
    private Integer sizeClass;

    @Column(name = "shield_hp")
    private Integer shieldHp;

    private String size;

    @Column(name = "crew_min")
    private Integer crewMin;

    private Float mass;

    @Column(name = "cargo_capacity")
    private Float cargoCapacity;

    private Integer hp;

    @Column(name = "scm_speed")
    private Integer scmSpeed;

    @Column(name = "nav_max_speed")
    private Integer navMaxSpeed;

    private Float pitch;
    private Float yaw;
    private Float roll;

    @Column(name = "insurance_claim_time")
    private Float insuranceClaimTime;

    @Column(name = "production_status")
    private String productionStatus;

    @Column(name = "production_note")
    private String productionNote;

    private Double msrp;

    @Column(name = "pledge_url")
    private String pledgeUrl;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Lob
    @Column(name = "ship_image")
    private byte[] shipImage;
}
