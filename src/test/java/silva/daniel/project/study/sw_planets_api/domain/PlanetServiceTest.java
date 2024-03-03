package silva.daniel.project.study.sw_planets_api.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.*;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    @InjectMocks
    private PlanetService service;

    @Mock
    private PlanetRepository repository;

    @Test
    void createPlanet_WithValidData_ReturnsPlanet() {
        var planetMock = PLANET;
        planetMock.setId(1L);
        when(repository.save(PLANET)).thenReturn(planetMock);
        var sut = service.create(PLANET);

        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    void createPlanet_WithInvalidData_ThrowsException() {
        when(repository.save(INVALID_PLANET)).thenThrow(new RuntimeException("Error"));

        assertThatThrownBy(() -> service.create(INVALID_PLANET))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getPlanet_ByExistingId_ReturnsPlanet() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(PLANET_WITH_ID));

        var sut = service.getById(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET_WITH_ID);
    }

    @Test
    void getPlanet_ByUnexistingId_ReturnsEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var sut = service.getById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    void getPlanet_ByExistingName_ReturnsPlanet() {
        when(repository.findByName(PLANET_WITH_ID.getName())).thenReturn(Optional.of(PLANET_WITH_ID));

        var sut = service.getByName(PLANET_WITH_ID.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET_WITH_ID);
    }

    @Test
    void getPlanet_ByUnexistingName_ReturnsEmpty() {
        var nameUnexisting = "Unexisting name";
        when(repository.findByName(nameUnexisting)).thenReturn(Optional.empty());

        var sut = service.getByName(nameUnexisting);
        assertThat(sut).isEmpty();
    }

    @Test
    void getPlanets_ReturnAllPlanets() {
        List<Planet> planetList = new ArrayList<>() {
            {
                add(PLANET_WITH_ID);
            }
        };
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET_WITH_ID.getName(), PLANET_WITH_ID.getTerrain()));

        when(repository.findAll(query)).thenReturn(planetList);

        var sutWithFilter = service.list(PLANET_WITH_ID.getName(), PLANET_WITH_ID.getTerrain());
        assertThat(sutWithFilter).isNotEmpty();
        assertThat(sutWithFilter).hasSize(1);
        assertThat(sutWithFilter).contains(PLANET_WITH_ID);
    }

    @Test
    void getPlanets_ReturnNoPlanets() {
        when(repository.findAll(any())).thenReturn(Collections.emptyList());
        var sut = service.list(null, null);

        assertThat(sut).isEmpty();

        var sutWithFilter = service.list(PLANET_WITH_ID.getName(), PLANET_WITH_ID.getTerrain());
        assertThat(sutWithFilter).isEmpty();

        var sutWithOnlyName = service.list(PLANET_WITH_ID.getName(), null);
        assertThat(sutWithOnlyName).isEmpty();

        var sutWithOnlyTerrain = service.list(null, PLANET_WITH_ID.getTerrain());
        assertThat(sutWithOnlyTerrain).isEmpty();
    }


}