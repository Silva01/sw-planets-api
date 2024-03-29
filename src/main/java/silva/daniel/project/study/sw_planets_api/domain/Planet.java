package silva.daniel.project.study.sw_planets_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang.builder.EqualsBuilder;
import silva.daniel.project.study.sw_planets_api.jacoco.ExcludeFromJacocoGeneratedReport;

@Entity
@Table(name = "planets", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String climate;

    @NotEmpty
    @Column(nullable = false)
    private String terrain;

    public Planet(String name, String climate, String terrain) {
        this(null, name, climate, terrain);
    }

    public Planet(Long id, String name, String climate, String terrain) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet() {

    }

    public Planet(String name, String terrain) {
        this(null, name, null, terrain);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public String toString() {
        return super.toString();
    }
}
