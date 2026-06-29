package est.una.ac.cr.backend.controller;
import est.una.ac.cr.backend.dto.OficinaType;
import est.una.ac.cr.backend.entity.Oficina;
import est.una.ac.cr.backend.service.OficinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController

@RequestMapping("/api/oficinas")
public class OficinaController {

    @Autowired
    private OficinaService oficinaService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OficinaType> crearOficina(@RequestBody Oficina oficina) {
        Oficina creada = oficinaService.insertaroficina(oficina);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OficinaType(creada));
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OficinaType> actualizarOficina(@PathVariable Integer id, @RequestBody Oficina oficinaActualizada) {
        Oficina actualizada = oficinaService.editarOficina(id,oficinaActualizada);
        return ResponseEntity.status(HttpStatus.OK).body(new OficinaType(actualizada));

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarOficina(@PathVariable Integer id) {
        oficinaService.eliminaroficina(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<OficinaType> obtenerOficinas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return oficinaService.listarOficinas(pageable).map(OficinaType::new);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OficinaType> obtenerPorId(@PathVariable Integer id) {
        OficinaType oficina = new OficinaType(oficinaService.buscarOficina(id));
        return ResponseEntity.ok(oficina);
    }
}

