package est.una.ac.cr.backend.dto;

import est.una.ac.cr.backend.entity.Oficina;
import est.una.ac.cr.backend.entity.Persona;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OficinaType", propOrder = {
        "id",
        "nombre",
        "direccion",
        "cantidad_maxima",
        "ingresos_activos",
        "personas"
})
public class OficinaType {

    private Integer id;
    private String nombre;
    private String direccion;
    private Integer cantidad_maxima;
    private Integer ingresos_activos;
    private List<String> personas;

    public OficinaType(Oficina oficina) {
        this.id = oficina.getId();
        this.nombre = oficina.getNombre();
        this.direccion = oficina.getDireccion();
        this.cantidad_maxima = oficina.getCantidad_maxima();
        this.ingresos_activos = oficina.getIngresos_activos();

        if (oficina.getPersonas() != null) {
            this.personas = oficina.getPersonas()
                    .stream()
                    .map(Persona::getNombre)
                    .collect(Collectors.toList());
        } else {
            this.personas = new ArrayList<>(); // lista vacía
        }
    }

}

