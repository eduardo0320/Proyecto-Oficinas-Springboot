package est.una.ac.cr.backend.service;

import est.una.ac.cr.backend.entity.Persona;
import est.una.ac.cr.backend.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public Page<Persona> listarPersonas(Pageable pageable) {

        return personaRepository.findAll(pageable);

    }

    public Persona buscarPersona(Integer id) {

        return personaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Persona no encontrada"));

    }

    public Persona buscarPersonaPorNombre(String nombre) {

        return personaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Persona no encontrada"));

    }

    public Persona editarPersona(Integer id, Persona personaActualizada) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Persona no encontrada"));

        boolean cambiaDeOficina = persona.getOficina() != null
                && personaActualizada.getOficina() != null
                && !persona.getOficina().getId().equals(personaActualizada.getOficina().getId());

        if (cambiaDeOficina && Boolean.TRUE.equals(persona.getEnOficina())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La persona está actualmente en una oficina. Registre su salida antes de cambiar de oficina.");
        }

        persona.setNombre(personaActualizada.getNombre());
        persona.setEmail(personaActualizada.getEmail());
        persona.setDireccion(personaActualizada.getDireccion());
        persona.setFechaNacimiento(personaActualizada.getFechaNacimiento());
        persona.setIdUsuario(personaActualizada.getIdUsuario());
        persona.setOficina(personaActualizada.getOficina());
        persona.setTelefono(personaActualizada.getTelefono());
        persona.setCargo(personaActualizada.getCargo());
        persona.setEstado(personaActualizada.getEstado());

        return personaRepository.save(persona);
    }

    public Page<Persona> obtenerPersonasEnOficina(Pageable pageable) {
        return personaRepository.findPersonasEnOficina(pageable);
    }

    public Persona insertarPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public Page<Persona> buscarPersonasPorCategoria(String categoria, String busqueda, Pageable pageable) {
        String categoriaNormalizada = categoria == null ? "" : categoria.trim().toLowerCase(Locale.ROOT);
        String busquedaNormalizada = busqueda == null ? "" : busqueda.trim();

        if (busquedaNormalizada.isEmpty()) {
            return Page.empty(pageable);
        }

        return switch (categoriaNormalizada) {

            case "idusuario"  -> personaRepository.findByIdUsuarioContainingIgnoreCase(busquedaNormalizada, pageable);
            case "nombre"     -> personaRepository.findByNombreContainingIgnoreCase(busquedaNormalizada, pageable);
            case "email"      -> personaRepository.findByEmailContainingIgnoreCase(busquedaNormalizada, pageable);
            case "direccion"  -> personaRepository.findByDireccionContainingIgnoreCase(busquedaNormalizada, pageable);
            case "telefono"   -> personaRepository.findByTelefonoContainingIgnoreCase(busquedaNormalizada, pageable);
            case "cargo"      -> personaRepository.findByCargoContainingIgnoreCase(busquedaNormalizada, pageable);
            case "estado"     -> personaRepository.findByEstadoContainingIgnoreCase(busquedaNormalizada, pageable);
            case "oficina"    -> personaRepository.findByOficina_NombreContainingIgnoreCase(busquedaNormalizada, pageable);

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Categoría de búsqueda no soportada: " + categoria);
        };
    }
    public void eliminarPersona(Integer id) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Persona no encontrada"));


        if (persona.getEnOficina()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Error: Registre salida antes de eliminar a la persona.");
        }

        personaRepository.delete(persona);
    }

    public void cambiarEstadoEnOficina(Persona personaActualizada, String tipoRegistro) {
        if ("Entrada".equals(tipoRegistro)) {
            personaActualizada.setEnOficina(true);
            personaRepository.save(personaActualizada);
            return;
        }
        if ("Salida".equals(tipoRegistro)) {
            personaActualizada.setEnOficina(false);
            personaRepository.save(personaActualizada);

        }
    }

}