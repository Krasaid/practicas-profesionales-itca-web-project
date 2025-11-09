//UpdateEstadoRequest dto
package sv.edu.itca.practicas_profesionales_itca_web.dto;

import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import lombok.Data;

@Data
public class UpdateEstadoRequest {
    private EstadoPropuesta nuevoEstado;
    private String explicacion;
}