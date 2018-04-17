package com.thalesgroup.scadasoft.opmmgt.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author JC Menchi
 *
 * This class represent a Action of the system
 *
 */
@Entity
@Table(name="opmaction")
public class Action {

    @Id
    @SequenceGenerator(name="ACT_GEN", sequenceName="opmaction_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACT_GEN")
    @Column(name="action_id")
    private Integer id;

    @NotNull
    private Integer category;
    @NotNull
    private String name;
    private String description;
    private String abbrev;

    public Action() {
    }

    public Action(Integer c, String name, String description) {
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

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String a) {
        this.abbrev = a;
    }
    
    @Override
    public String toString() {
        return String.format(
                "Action[cat=%d, name='%s', abbrev='%s', desc='%s']",
                category, name, abbrev, description);
    }

}
