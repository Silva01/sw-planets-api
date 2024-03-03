package silva.daniel.project.study.sw_planets_api.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlanetRepository extends CrudRepository<Planet, Long> {
    Optional<Object> findByName(String name);
}
