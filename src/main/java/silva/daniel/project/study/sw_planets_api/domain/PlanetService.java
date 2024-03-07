package silva.daniel.project.study.sw_planets_api.domain;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<Planet> getByName(String name) {
        return planetRepository.findByName(name);
    }

    public List<Planet> list(final String name, final String terrain) {
        Example<Planet> example = QueryBuilder.makeQuery(new Planet(name, terrain));
        return planetRepository.findAll(example);
    }

    public void delete(Long id) {
        planetRepository.deleteById(id);
    }
}
