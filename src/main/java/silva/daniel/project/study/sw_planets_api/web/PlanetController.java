package silva.daniel.project.study.sw_planets_api.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silva.daniel.project.study.sw_planets_api.domain.Planet;
import silva.daniel.project.study.sw_planets_api.domain.PlanetService;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetService planetService;

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planetService.create(planet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> get(@PathVariable("id") Long id) {
        return planetService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable("name") String name) {
        return planetService.getByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getAll(@RequestParam(required = false) String name, @RequestParam(required = false) String terrain) {
        return ResponseEntity.ok(planetService.list(name, terrain));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        planetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
