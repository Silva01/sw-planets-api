package silva.daniel.project.study.sw_planets_api.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

}