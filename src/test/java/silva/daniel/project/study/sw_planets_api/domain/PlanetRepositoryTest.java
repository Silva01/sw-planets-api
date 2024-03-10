package silva.daniel.project.study.sw_planets_api.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.PLANET;

@DataJpaTest
class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    void afterSetUp() {
        PLANET.setId(null);
    }

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

    @ParameterizedTest
    @MethodSource("provideInvalidPlanetData")
    void createPlanet_WithInvalidData_ThrowsException(Planet planet) {
        assertThatThrownBy(() -> planetRepository.save(planet))
                .isInstanceOf(RuntimeException.class);
    }

    private static Stream<Arguments> provideInvalidPlanetData() {
        return Stream.of(
                Arguments.of(new Planet("", "arid", "desert")),
                Arguments.of(new Planet("Tatooine", "", "desert")),
                Arguments.of(new Planet("Tatooine", "arid", "")),
                Arguments.of(new Planet("", "", "desert")),
                Arguments.of(new Planet("", "arid", "")),
                Arguments.of(new Planet("Tatooine", "", "")),
                Arguments.of(new Planet("", "", "")),
                Arguments.of(new Planet(null, "arid", "desert")),
                Arguments.of(new Planet("Tatooine", null, "desert")),
                Arguments.of(new Planet("Tatooine", "arid", null)),
                Arguments.of(new Planet(null, null, "desert")),
                Arguments.of(new Planet(null, "arid", null)),
                Arguments.of(new Planet("Tatooine", null, null)),
                Arguments.of(new Planet(null, null, null))
        );
    }

    @Test
    void createPlanet_WithExistingName_ThrowsException() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getPlanet_ByExistingId_ReturnsPlanet() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);

        var sut = planetRepository.findById(planet.getId());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    void getPlanet_ByUnexistingId_ReturnsNotFound() {
        var sut = planetRepository.findById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    void getPlanet_ByExistingName_ReturnsPlanet() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);

        var sut = planetRepository.findByName(planet.getName());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(planet);
    }

    @Test
    void getPlanet_ByUnexistingName_ReturnsNotFound() {
        var sut = planetRepository.findByName("Tatooine");
        assertThat(sut).isEmpty();
    }

    @Test
    @Sql("/import_planets.sql")
    void listPlanets_ReturnsFilteredPlanets() {
        Example<Planet> exampleName = QueryBuilder.makeQuery(new Planet("Alderaan", null));
        Example<Planet> exampleTerrain = QueryBuilder.makeQuery(new Planet(null, "jungle, rainforests"));
        Example<Planet> exampleFull = QueryBuilder.makeQuery(new Planet(null, null));

        var sutName = planetRepository.findAll(exampleName);
        assertThat(sutName).hasSize(1);
        assertThat(sutName.get(0).getName()).isEqualTo("Alderaan");

        var sutTerrain = planetRepository.findAll(exampleTerrain);
        assertThat(sutTerrain).hasSize(1);
        assertThat(sutTerrain.get(0).getTerrain()).isEqualTo("jungle, rainforests");
        var sutFull = planetRepository.findAll(exampleFull);
        assertThat(sutFull).hasSize(3);
        assertThat(sutFull.get(0).getName()).isEqualTo("Tatooine");
        assertThat(sutFull.get(1).getName()).isEqualTo("Alderaan");
        assertThat(sutFull.get(2).getName()).isEqualTo("Yavin IV");

    }

    @Test
    void listPlanets_ReturnsNoPlanets() {
        Example<Planet> exampleFull = QueryBuilder.makeQuery(new Planet(null, null));

        var sutName = planetRepository.findAll(exampleFull);
        assertThat(sutName).isEmpty();
    }

    @Test
    void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);

        planetRepository.deleteById(planet.getId());

        var sut = testEntityManager.find(Planet.class, planet.getId());
        assertThat(sut).isNull();
    }

    // TODO: Isso aqui não funciona, pois no spring 3 não é possível capturar a exceção EmptyResultDataAccessException
//    @Test
//    void removePlanet_WithUnexistingId_ThrowsException() {
//        assertThatThrownBy(() -> planetRepository.deleteById(10L))
//                .isInstanceOf(EmptyResultDataAccessException.class);
//
//    }


}