package est.una.ac.cr.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private String direccion;
    private Integer cantidad_maxima;
    private Integer ingresos_activos;

    @OneToMany(mappedBy = "oficina")
    private List<Persona> personas;

    @PrePersist
    public void prePersist() {
        if(direccion == null) direccion = "";
        if (cantidad_maxima == null) cantidad_maxima = 0;
        if (ingresos_activos == null) ingresos_activos = 0;

    }

}



