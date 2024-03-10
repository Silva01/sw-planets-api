package silva.daniel.project.study.sw_planets_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import silva.daniel.project.study.sw_planets_api.domain.Planet;

import static org.assertj.core.api.Assertions.assertThat;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.*;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PlanetIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createPlanet_ReturnsCreated() {
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody()).isNotNull();
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    void getPlanet_ReturnsPlanet() {
        var sut = restTemplate.getForEntity("/planets/1", Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    void getPlanetByName_ReturnsPlanet() {
        var sut = restTemplate.getForEntity("/planets/name/Tatooine", Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    void listPlanets_ReturnsAllPlanets() {
        var sut = restTemplate.getForEntity("/planets", Planet[].class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isNotNull();
        assertThat(sut.getBody()).hasSize(3);
    }

    @Test
    void listPlanets_ByName_ReturnsPlanets() {
        var sut = restTemplate.getForEntity("/planets?name=Tatooine", Planet[].class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isNotNull();
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0].getId()).isEqualTo(TATOOINE.getId());
        assertThat(sut.getBody()[0].getName()).isEqualTo(TATOOINE.getName());
        assertThat(sut.getBody()[0].getTerrain()).isEqualTo(TATOOINE.getTerrain());
        assertThat(sut.getBody()[0].getClimate()).isEqualTo(TATOOINE.getClimate());
    }

    @Test
    void listPlanets_ByTerrain_ReturnsPlanets() {
        var sut = restTemplate.getForEntity("/planets?terrain=grasslands, mountains", Planet[].class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isNotNull();
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0].getId()).isEqualTo(ALDERAAN.getId());
        assertThat(sut.getBody()[0].getName()).isEqualTo(ALDERAAN.getName());
        assertThat(sut.getBody()[0].getTerrain()).isEqualTo(ALDERAAN.getTerrain());
        assertThat(sut.getBody()[0].getClimate()).isEqualTo(ALDERAAN.getClimate());
    }

    @Test
    void removePlanet_ReturnsNoContent() {
        var sut = restTemplate.exchange("/planets?id=1", HttpMethod.DELETE, null, Void.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}