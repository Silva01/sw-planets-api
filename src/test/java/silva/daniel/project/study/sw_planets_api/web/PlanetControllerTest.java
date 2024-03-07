package silva.daniel.project.study.sw_planets_api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.web.servlet.MockMvc;
import silva.daniel.project.study.sw_planets_api.domain.Planet;
import silva.daniel.project.study.sw_planets_api.domain.PlanetService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.PLANET;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.PLANETS;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetService planetService;

    @Test
    void createPlanet_WithValidData_ReturnsCreated() throws Exception {
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(post("/planets")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PLANET)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
        var planetEmpty = new Planet();
        var invalidPlanet = new Planet("", "", "");

        mockMvc.perform(post("/planets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planetEmpty)))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(post("/planets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPlanet)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createPlanet_WithInvalidData_ReturnsConflict() throws Exception {
        when(planetService.create(PLANET)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/planets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PLANET)))
                .andExpect(status().isConflict());
    }

    @Test
    void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        when(planetService.getById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/{id}", 1)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {
        when(planetService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/{id}", 1)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetService.getByName("Tatooine")).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/{name}", "Tatooine")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception {
        when(planetService.getByName("Tatooine")).thenReturn(Optional.empty());

        mockMvc.perform(get("/planets/name/{name}", "Tatooine")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(PLANET.getName(), PLANET.getTerrain())).thenReturn(List.of(PLANET));

        mockMvc.perform(get("/planets")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value(PLANETS.get(0)))
                .andExpect(jsonPath("$[1]").value(PLANETS.get(1)))
                .andExpect(jsonPath("$[2]").value(PLANETS.get(2)));

        mockMvc.perform(get("/planets" + String.format("?name=%s&terrain=%s", PLANET.getName(), PLANET.getTerrain()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(PLANET));
    }

    @Test
    void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.list(anyString(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/planets")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/planets?id=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(planetService).delete(anyLong());

        mockMvc.perform(delete("/planets?id=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}