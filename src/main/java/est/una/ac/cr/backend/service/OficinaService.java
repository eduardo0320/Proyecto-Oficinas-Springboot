package est.una.ac.cr.backend.service;


import est.una.ac.cr.backend.dto.OficinaType;
import est.una.ac.cr.backend.dto.PersonaType;
import est.una.ac.cr.backend.entity.Oficina;
import est.una.ac.cr.backend.entity.Persona;
import est.una.ac.cr.backend.repository.OficinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OficinaService {

    @Autowired
    private OficinaRepository oficinaRepository;


    public Page<Oficina> listarOficinas(Pageable pageable) {
        return oficinaRepository.findAll(pageable);
    }

    public Oficina buscarOficina(int id) {
        return oficinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "oficina no encontrada"));
    }

    public Oficina insertaroficina(Oficina oficina) {
        return oficinaRepository.save(oficina);
    }

    public void eliminaroficina(int id) {
        oficinaRepository.deleteById(id);
    }
    public Oficina editarOficina(Integer id, Oficina oficinaActualizada) {

        Oficina oficina = buscarOficina(id);

        oficina.setNombre(oficinaActualizada.getNombre());
        oficina.setIngresos_activos(oficinaActualizada.getIngresos_activos());
        oficina.setCantidad_maxima(oficinaActualizada.getCantidad_maxima());
        oficina.setDireccion(oficinaActualizada.getDireccion());
        oficina.setCantidad_maxima(oficinaActualizada.getCantidad_maxima());
        oficina.setPersonas(oficinaActualizada.getPersonas());

        return oficinaRepository.save(oficina);

    }

    public void cambiarEstadoIngresos(Oficina oficinaActualizada, String tipoRegistro) {

        int ingresos_activos = oficinaActualizada.getIngresos_activos();

        if("Salida".equals(tipoRegistro)) {
            oficinaActualizada.setIngresos_activos(ingresos_activos-1);
            oficinaRepository.save(oficinaActualizada);
            return;
        }
        if("Entrada".equals(tipoRegistro)) {
            oficinaActualizada.setIngresos_activos(ingresos_activos+1);
            oficinaRepository.save(oficinaActualizada);

        }
    }
}