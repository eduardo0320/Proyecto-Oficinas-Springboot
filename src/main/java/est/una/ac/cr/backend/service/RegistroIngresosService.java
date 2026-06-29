package est.una.ac.cr.backend.service;
import est.una.ac.cr.backend.entity.Oficina;
import est.una.ac.cr.backend.entity.Persona;
import est.una.ac.cr.backend.entity.RegistroIngresos;
import est.una.ac.cr.backend.repository.Registro_IngresosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RegistroIngresosService {

    @Autowired
    private Registro_IngresosRepository registro_IngresosRepository;
    @Autowired
    private OficinaService oficinaService;
    @Autowired
    private PersonaService personaService;



    public Page<RegistroIngresos> listarRegistros( Pageable pageable) {
        return registro_IngresosRepository.findAll(pageable);
    }

    @Transactional
    public void agregarRegistro(RegistroIngresos registro) {


        Persona persona = personaService.buscarPersonaPorNombre(registro.getNombrePersona());
        Oficina oficina = oficinaService.buscarOficina(persona.getOficina().getId());

        registro.setNombreOficina(oficina.getNombre());

        int ingresosMaximos = oficina.getCantidad_maxima();
        int ingresosActivos = oficina.getIngresos_activos();
        String tipoRegistro = registro.getTipo();

        switch(tipoRegistro) {
            case "Entrada":
                if(persona.getEnOficina()){
                    throw new IllegalStateException("No se puede agregar un ingreso: La persona ya está en una oficina");

                }
                if(ingresosActivos >= ingresosMaximos){
                    throw new IllegalStateException("La oficina alcanzó su capacidad máxima");

                }
                oficinaService.cambiarEstadoIngresos(oficina, tipoRegistro);
                personaService.cambiarEstadoEnOficina(persona, tipoRegistro);
                break;

            case "Salida":
                if(!persona.getEnOficina()){
                    throw new IllegalStateException("No se puede agregar salida: La persona no está en una oficina");

                }
                oficinaService.cambiarEstadoIngresos(oficina, tipoRegistro);
                personaService.cambiarEstadoEnOficina(persona, tipoRegistro);
                break;

            default:
                    throw new IllegalArgumentException("Tipo de registro inválido.");

        }
            registro_IngresosRepository.save(registro);

    }

    public void eliminarRegistro(Integer id) {

        RegistroIngresos registro = registro_IngresosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Registro no encontrado"));

        registro_IngresosRepository.delete(registro);
    }

    public RegistroIngresos buscarRegistro(Integer id) {

        return registro_IngresosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Registro no encontrado"));

    }
}