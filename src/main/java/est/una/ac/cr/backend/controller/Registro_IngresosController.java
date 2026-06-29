package est.una.ac.cr.backend.controller;
import est.una.ac.cr.backend.dto.Registro_IngresosType;
import est.una.ac.cr.backend.entity.Oficina;
import est.una.ac.cr.backend.entity.RegistroIngresos;
import est.una.ac.cr.backend.repository.OficinaRepository;
import est.una.ac.cr.backend.repository.PersonaRepository;
import est.una.ac.cr.backend.repository.Registro_IngresosRepository;
import est.una.ac.cr.backend.service.PersonaService;
import est.una.ac.cr.backend.service.RegistroIngresosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.*;



import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/registros")
public class Registro_IngresosController {

    @Autowired
    private Registro_IngresosRepository registro_IngresosRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private RegistroIngresosService registroIngresosService;
    @Autowired
    private OficinaRepository oficinaRepository;
    private PersonaService personaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public Page<Registro_IngresosType> obtenerRegistros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return registroIngresosService.listarRegistros(pageable).map(Registro_IngresosType::new);
    }


    @GetMapping("/obtener/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public ResponseEntity<Registro_IngresosType> obtenerRegistroPorId(@PathVariable Integer id) {
        RegistroIngresos registro = registroIngresosService.buscarRegistro(id);
        return ResponseEntity.ok(new Registro_IngresosType(registro));
    }

    @PostMapping("/registros/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public ResponseEntity<Void> crearRegistro(@RequestBody RegistroIngresos registro) {
        registroIngresosService.agregarRegistro(registro);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Integer id) {
       registroIngresosService.eliminarRegistro(id);
       return ResponseEntity.ok().build();
    }


    @GetMapping("/registros/top3-entradas")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public ResponseEntity<List<Map<String, Object>>> obtenerTop3Entradas() {
        List<Object[]> resultados = registro_IngresosRepository.findTop3PersonasConMasEntradas();

        List<Map<String, Object>> respuesta = resultados.stream().map(fila -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", fila[0]);
            map.put("totalEntradas", fila[1]);
            return map;
        }).toList();

        return ResponseEntity.ok(respuesta);
    }


    @GetMapping("/registros/ocupacion-maxima")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGISTRADOR')")
    public ResponseEntity<List<Map<String, Object>>> obtenerOcupacionMaximaOficinas() {
        List<Oficina> oficinas = oficinaRepository.findAll();

        List<Map<String, Object>> respuesta = oficinas.stream().map(oficina -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", oficina.getNombre());
            map.put("maximoIngresoSimultaneo", oficina.getCantidad_maxima());
            return map;
        }).toList();

        return ResponseEntity.ok(respuesta);
    }
}