package com.thalesgroup.scadasoft.opmmgt.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author JC Menchi
 *
 * This class represent a Location of the system.
 * Map to opmlocation table in database
 *
 * Tel jackson to ignore hibernateLazyInitializer and handler
 * because we use lazy loading in the mask class
 *
 */
@Entity
@Table(name="opmlocation")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location {

    @Id
    @SequenceGenerator(name="LOC_GEN", sequenceName="opmlocation_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOC_GEN")
    @Column(name="Location_id")
    private Integer id;

    @NotNull
    private Integer category;
    @NotNull
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="location_fk")
    private Set<Mask> masks;

    public Location() {
    }

    public Location(Integer c, String name, String description) {
        this.category = c;
        this.description = description;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCategory(int c) {
        this.category = c;
    }

    public Integer getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        this.description = d;
    }

    @Override
    public String toString() {
        return String.format(
                "Location[cat=%d, name='%s', desc='%s']",
                category, name, description);
    }

}
