package est.una.ac.cr.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data

public class RegistroIngresos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo;
    private LocalDate fecha;
    private LocalTime hora;

    private String nombrePersona;
    private String nombreOficina;

}
