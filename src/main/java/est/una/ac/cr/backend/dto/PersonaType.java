package est.una.ac.cr.backend.dto;
import est.una.ac.cr.backend.entity.Persona;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

import java.time.LocalDate;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonaType", propOrder = {
        "idAuto",
        "idUsuario",
        "nombre",
        "email",
        "direccion",
        "fechaNacimiento",
        "telefono",
        "cargo",
        "estado",
        "oficina",
        "enOficina",

})
public class PersonaType {

    private Integer idAuto;
    private String idUsuario;
    private String nombre;
    private String email;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String cargo;
    private String estado;
    private String oficina;
    private Boolean enOficina;

    public PersonaType(Persona persona) {
        this.idAuto = persona.getIdAuto();
        this.idUsuario = persona.getIdUsuario();
        this.nombre = persona.getNombre();
        this.email = persona.getEmail();
        this.direccion = persona.getDireccion();
        this.fechaNacimiento = persona.getFechaNacimiento();
        this.telefono = persona.getTelefono();
        this.cargo = persona.getCargo();
        this.estado = persona.getEstado();


        if(persona.getOficina() != null){
            this.oficina=persona.getOficina().getNombre();
        } else {
            this.oficina="";
        }
        this.enOficina = persona.getEnOficina();
    }

}