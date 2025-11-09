//Rol enum
package sv.edu.itca.practicas_profesionales_itca_web.model;

/**
 * Enumeración que define los roles de usuario permitidos en el sistema.
 * Se utiliza en la entidad {@link Usuario} para gestionar la autorización
 * y la lógica de negocio basada en permisos.
 */
public enum Rol {

    /**
     * Rol asignado a un estudiante.
     * Este rol permite al usuario crear y gestionar sus propias {@link Propuesta}s.
     */
    ALUMNO,

    /**
     * Rol asignado a un coordinador o docente.
     * Este rol permite al usuario revisar, aprobar o rechazar las {@link Propuesta}s
     * de los alumnos.
     */
    COORDINADOR
}