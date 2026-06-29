package est.una.ac.cr.backend.repository;

import est.una.ac.cr.backend.entity.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    @Query("SELECT p FROM Persona p WHERE p.enOficina = true")
    Page<Persona> findPersonasEnOficina(Pageable pageable);

    Page<Persona> findByIdUsuarioContainingIgnoreCase(String idUsuario, Pageable pageable);

    Page<Persona> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Persona> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Persona> findByDireccionContainingIgnoreCase(String direccion, Pageable pageable);

    Page<Persona> findByTelefonoContainingIgnoreCase(String telefono, Pageable pageable);

    Page<Persona> findByCargoContainingIgnoreCase(String cargo, Pageable pageable);

    Page<Persona> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);

    Page<Persona> findByOficina_NombreContainingIgnoreCase(String oficinaNombre, Pageable pageable);

    Optional<Persona> findByNombre(String nombre);


}
