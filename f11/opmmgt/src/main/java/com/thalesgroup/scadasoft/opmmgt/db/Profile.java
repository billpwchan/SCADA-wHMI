package com.thalesgroup.scadasoft.opmmgt.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author JC Menchi
 *
 * This class represent a Profile of the system
 *
 */
@Entity
@Table(name="opmprofile")
public class Profile {

    @Id
    @SequenceGenerator(name="LOC_GEN", sequenceName="opmprofile_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOC_GEN")
    @Column(name="Profile_id")
    private Integer id;

    @NotNull
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="profile_fk")
    private Set<Mask> masks;


    public Profile() {
    }

    public Profile(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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

    public Set<Mask> getMasks() {
        return masks;
    }

    public void setDescription(String d) {
        this.description = d;
    }

    @Override
    public String toString() {
        return String.format(
                "Profile[name='%s', desc='%s']",
                name, description);
    }

}
