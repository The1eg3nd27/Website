package com.spectre.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "api_ships")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiShip {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;
   
    @JsonProperty("updated_at")
    private String updatedAt;



    private String size;
    private Double mass;
    private Double cargoCapacity;
    private Integer crewMin;
    private Integer crewMax;
    private Integer health;
    private Integer shieldHp;
    private Double scmSpeed;
    private Double maxSpeed;
    private Double pitch;
    private Double yaw;
    private Double roll;
    private String type;
    private String focus;
    private Double length;
    private Double beam;
    private Double height;
    private Integer sizeClass;
    private String pledgeUrl;
    private String link;


    
    @Column(columnDefinition = "TEXT")
    private String description;

    private String manufacturer;
    private Double insuranceClaimTime;
    private String productionStatus;

    @Column(columnDefinition = "TEXT")
    private String productionNote;

    private Double msrp;


    @Lob
    private byte[] image;

    public ApiShip() {}

    public Long getId() { return id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public Double getMass() { return mass; }
    public void setMass(Double mass) { this.mass = mass; }

    public Double getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(Double cargoCapacity) { this.cargoCapacity = cargoCapacity; }

    public Integer getCrewMin() { return crewMin; }
    public void setCrewMin(Integer crewMin) { this.crewMin = crewMin; }

    public Integer getCrewMax() { return crewMax; }
    public void setCrewMax(Integer crewMax) { this.crewMax = crewMax; }

    public Integer getHealth() { return health; }
    public void setHealth(Integer health) { this.health = health; }

    public Integer getShieldHp() { return shieldHp; }
    public void setShieldHp(Integer shieldHp) { this.shieldHp = shieldHp; }

    public Double getScmSpeed() { return scmSpeed; }
    public void setScmSpeed(Double scmSpeed) { this.scmSpeed = scmSpeed; }

    public Double getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(Double maxSpeed) { this.maxSpeed = maxSpeed; }

    public Double getPitch() { return pitch; }
    public void setPitch(Double pitch) { this.pitch = pitch; }

    public Double getYaw() { return yaw; }
    public void setYaw(Double yaw) { this.yaw = yaw; }

    public Double getRoll() { return roll; }
    public void setRoll(Double roll) { this.roll = roll; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public Double getInsuranceClaimTime() { return insuranceClaimTime; }
    public void setInsuranceClaimTime(Double insuranceClaimTime) { this.insuranceClaimTime = insuranceClaimTime; }

    public String getProductionStatus() { return productionStatus; }
    public void setProductionStatus(String productionStatus) { this.productionStatus = productionStatus; }

    public String getProductionNote() { return productionNote; }
    public void setProductionNote(String productionNote) { this.productionNote = productionNote; }

    public Double getMsrp() { return msrp; }
    public void setMsrp(Double msrp) { this.msrp = msrp; }

    public String getUpdatedAt() {return updatedAt;}
    
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getFocus() { return focus; }
    public void setFocus(String focus) { this.focus = focus; }

    public Double getLength() { return length; }
    public void setLength(Double length) { this.length = length; }

    public Double getBeam() { return beam; }
    public void setBeam(Double beam) { this.beam = beam; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Integer getSizeClass() {return sizeClass;}
    public void setSizeClass(Integer sizeClass) {this.sizeClass = sizeClass;}
    
    public String getPledgeUrl() {return pledgeUrl;}
    public void setPledgeUrl(String pledgeUrl) {this.pledgeUrl = pledgeUrl;}

    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}

    
    
}
