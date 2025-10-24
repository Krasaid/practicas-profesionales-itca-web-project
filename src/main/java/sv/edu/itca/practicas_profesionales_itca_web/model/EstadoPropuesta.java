package sv.edu.itca.practicas_profesionales_itca_web.model;

/**
 * Enumeración que define los posibles estados del ciclo de vida
 * de una Propuesta
 */
public enum EstadoPropuesta {

    /**
     * La propuesta ha sido revisada y aceptada por un coordinador.
     * Es un estado final.
     */
    APROBADO,

    /**
     * La propuesta ha sido revisada y rechazada por un coordinador.
     * Debería ir acompañada de una PropuestaexplicacionRechazo
     * Es un estado final.
     */
    DENEGADO,

    /**
     * Estado inicial de la propuesta.
     * La propuesta ha sido enviada por el alumno y está pendiente
     * de la evaluación de un coordinador.
     */
    EN_REVISION
}