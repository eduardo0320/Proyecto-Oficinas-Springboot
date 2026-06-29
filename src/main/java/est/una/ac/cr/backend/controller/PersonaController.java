package est.una.ac.cr.backend.controller;

import est.una.ac.cr.backend.dto.PersonaType;
import est.una.ac.cr.backend.entity.Persona;
import est.una.ac.cr.backend.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonaType> crearPersona(@RequestBody Persona persona) {
        Persona creada = personaService.insertarPersona(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PersonaType(creada));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonaType> actualizarPersona(@PathVariable Integer id,
            @RequestBody Persona personaActualizada) {
        Persona actualizada = personaService.editarPersona(id, personaActualizada);
        return ResponseEntity.status(HttpStatus.OK).body(new PersonaType(actualizada));

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Integer id) {
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<PersonaType> listarPersonas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return personaService.listarPersonas(pageable).map(PersonaType::new);
    }

    @GetMapping("/registros/en-oficina")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public Page<PersonaType> obtenerPersonasEnOficina() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Persona> personas = personaService.obtenerPersonasEnOficina(pageable);
        return personas.map(PersonaType::new);
    }

    @GetMapping("/search/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PersonaType> buscarPersona(@PathVariable Integer id) {
        Persona persona = personaService.buscarPersona(id);
        return ResponseEntity.ok(new PersonaType(persona));
    }

    @GetMapping("/buscar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> buscarPersonasPorCategoria(
            @RequestParam String categoria,
            @RequestParam String busqueda,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Persona> resultado = personaService.buscarPersonasPorCategoria(categoria, busqueda, pageable);

        List<PersonaType> personas = resultado.getContent()
                .stream()
                .map(PersonaType::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("personas", personas);
        response.put("totalPages", resultado.getTotalPages());
        response.put("totalElements", resultado.getTotalElements());
        response.put("currentPage", resultado.getNumber());

        return ResponseEntity.ok(response);
    }

}
