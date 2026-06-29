package est.una.ac.cr.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;



@Entity
@Data
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAuto;

    private String idUsuario;
    private String nombre;
    private String email;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String cargo;
    private String estado;
    private Boolean enOficina = false;


    @ManyToOne
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;

    @PrePersist
    public void prePersist() {
        if (enOficina == null) enOficina = false;

    }

//
}
