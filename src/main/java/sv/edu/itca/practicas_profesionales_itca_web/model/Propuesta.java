package sv.edu.itca.practicas_profesionales_itca_web.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString; // Importante para la buena práctica
import java.time.LocalDate;

/**
 * Entidad que representa una Propuesta de prácticas profesionales.
 * Se mapea a la tabla "propuestas" y está fuertemente vinculada a un Usuario (alumno).
 */
@Data
@Entity
@Table(name = "propuestas")
@ToString(exclude = "alumno")
public class Propuesta {

    /**
     * Identificador único de la propuesta (PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación "Muchos a Uno" con Usuario.
     * Múltiples propuestas pueden pertenecer a un único alumno.
     * Esta entidad es la "dueña" de la relación.
     */
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false) // Esta es la columna de llave foránea
    private Usuario alumno;

    /**
     * Estado actual de la propuesta (ej. PENDIENTE, APROBADA, RECHAZADA).
     * Se almacena como texto (STRING) en la BD.
     */
    @Enumerated(EnumType.STRING)
    private EstadoPropuesta estado;

    /**
     * Nombre de la empresa donde se realizarán las prácticas.
     */
    private String empresa;

    /**
     * Fecha en que la propuesta fue enviada.
     */
    private LocalDate fechaEnvio;

    /**
     * Número de horas propuestas para las prácticas.
     */
    private int horasPropuestas;

    /**
     * Campo opcional para detallar la razón del rechazo,
     * en caso de que el 'estado' sea RECHAZADA.
     */
    private String explicacionRechazo;
}