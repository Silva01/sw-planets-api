package silva.daniel.project.study.sw_planets_api.domain;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    public Optional<Planet> getById(Long Id) {
        return planetRepository.findById(Id);
    }

    public Optional<Object> getByName(String name) {
        return planetRepository.findByName(name);
    }
}
