package silva.daniel.project.study.sw_planets_api.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.PLANET;

@DataJpaTest
class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void createPlanet_WithValidData_ReturnsPlanet() {
        var planet = planetRepository.save(PLANET);

        var sut = testEntityManager.find(Planet.class, planet.getId());
        assertThat(sut).isNotNull();

        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @Test
    void createPlanet_WithInvalidData_ThrowsException() {
        var planetEmpty = new Planet();
        var invalidPlanet = new Planet("", "", "");

        assertThatThrownBy(() -> planetRepository.save(planetEmpty))
                .isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> planetRepository.save(invalidPlanet))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createPlanet_WithExistingName_ThrowsException() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet))
                .isInstanceOf(RuntimeException.class);
    }


}