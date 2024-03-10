package silva.daniel.project.study.sw_planets_api.commons;

import silva.daniel.project.study.sw_planets_api.domain.Planet;

import java.util.List;

public class PlanetCommonsConstantEnum {

    public static final Planet PLANET = new Planet("planet", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
    public static final Planet PLANET_WITH_ID = new Planet(1L, "Tatooine", "arid", "desert");

    public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet(2L, "Alderaan", "Temperate", "grasslands, mountains");
    public static final Planet YAVIN_IV = new Planet(3L, "Yavin IV", "Temperate, tropical", "jungle, rainforests");

    public static final List<Planet> PLANETS = List.of(TATOOINE, ALDERAAN, YAVIN_IV);
}
