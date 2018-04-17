package com.thalesgroup.scadasoft.opmmgt.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author JC Menchi
 *
 * This class represent a Mask of the system
 *
 */
@Entity
@Table(name="opmmask")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Mask {

    @Id
    @SequenceGenerator(name="MASK_GEN", sequenceName="opmmask_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MASK_GEN")
    @Column(name="Mask_id")
    private Integer id;

    @NotNull
    private String mask1;
    private String mask2;
    private String mask3;
    private String mask4;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="function_fk", nullable = true)
    private Function function;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="location_fk", nullable = true)
    private Location location;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="profile_fk", nullable = true)
    @JsonBackReference
    private Profile profile;

    public Mask() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getMask1() {
        return mask1;
    }

    public void setMask1(String mask) {
        this.mask1 = mask;
    }

    public String getMask2() {
        return mask2;
    }

    public void setMask2(String mask) {
        this.mask2 = mask;
    }

    public String getMask3() {
        return mask3;
    }

    public void setMask3(String mask) {
        this.mask3 = mask;
    }

    public String getMask4() {
        return mask4;
    }

    public void setMask4(String mask) {
        this.mask4 = mask;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
