package est.una.ac.cr.backend.repository;

import est.una.ac.cr.backend.entity.RegistroIngresos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Registro_IngresosRepository extends JpaRepository<RegistroIngresos, Integer> {

    @Query(value = """
        SELECT p.nombre, COUNT(*) as total
        FROM registro_ingresos r
        JOIN persona p ON r.persona_id = p.id_auto
        WHERE r.tipo = 'Entrada'
        GROUP BY p.nombre
        ORDER BY total DESC
        LIMIT 3
        """, nativeQuery = true)
    List<Object[]> findTop3PersonasConMasEntradas();


}
