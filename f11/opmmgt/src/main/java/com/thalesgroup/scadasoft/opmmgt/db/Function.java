package com.thalesgroup.scadasoft.opmmgt.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author JC Menchi
 *
 * This class represent a Function of the system
 *
 */
@Entity
@Table(name="opmfunction")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Function {

    @Id
    @SequenceGenerator(name="FCT_GEN", sequenceName="opmfunction_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCT_GEN")
    @Column(name="function_id")
    private Integer id ;

    @NotNull
    private Integer category;
    @NotNull
    private String name;
    private String description;
    private String family;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="function_fk")
    private Set<Mask> masks;

    public Function() {
    }

    public Function(Integer id) {
        this.id = id;
    }

    public Function(Integer c, String name, String description) {
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String f) {
        this.family = f;
    }

    @Override
    public String toString() {
        return String.format(
                "Function[cat=%d, name='%s', family='%s', desc='%s']",
                category, name, family, description);
    }

}
