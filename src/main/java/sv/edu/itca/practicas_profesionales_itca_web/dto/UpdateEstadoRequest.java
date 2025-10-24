//UpdateEstadoRequest dto
package sv.edu.itca.practicas_profesionales_itca_web.dto;

import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import lombok.Data;

/**
 * DTO usado por el Coordinador para aprobar o denegar propuestas.
 * Lombok genera automáticamente getters y setters.
 */
@Data
public class UpdateEstadoRequest {

    private EstadoPropuesta nuevoEstado; // Será APROBADO o DENEGADO
    private String explicacion; // Opcional, pero requerido si es DENEGADO
}
