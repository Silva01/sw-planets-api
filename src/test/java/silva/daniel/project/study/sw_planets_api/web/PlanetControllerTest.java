package silva.daniel.project.study.sw_planets_api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import silva.daniel.project.study.sw_planets_api.domain.Planet;
import silva.daniel.project.study.sw_planets_api.domain.PlanetService;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static silva.daniel.project.study.sw_planets_api.commons.PlanetCommonsConstantEnum.PLANET;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest {

    @Autowired
    private PlanetController planetController;

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

}