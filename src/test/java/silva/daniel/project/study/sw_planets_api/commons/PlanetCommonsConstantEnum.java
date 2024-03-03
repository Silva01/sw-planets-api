package silva.daniel.project.study.sw_planets_api.commons;

import silva.daniel.project.study.sw_planets_api.domain.Planet;

public class PlanetCommonsConstantEnum {

    public static final Planet PLANET = new Planet("Tatooine", "arid", "desert");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
    public static final Planet PLANET_WITH_ID = new Planet(1L, "Tatooine", "arid", "desert");
}
