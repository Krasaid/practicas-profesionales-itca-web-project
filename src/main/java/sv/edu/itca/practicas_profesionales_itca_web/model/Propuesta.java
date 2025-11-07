package sv.edu.itca.practicas_profesionales_itca_web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate; // <-- Asegúrate de tener este import

@Data
@Entity
@Table(name = "propuestas")
public class Propuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    @Enumerated(EnumType.STRING)
    private EstadoPropuesta estado;

    @Column(nullable = false) // Hacemos que la empresa sea obligatoria
    private String empresa;

    private LocalDate fechaEnvio;

    @Column(nullable = false) // Hacemos las horas obligatorias
    private int horasPropuestas;

    private String explicacionRechazo;

    // --- ¡CAMPOS NUEVOS AÑADIDOS! ---

    /**
     * Descripción detallada de las actividades a realizar.
     * Usamos "TEXT" para permitir descripciones largas (más de 255 caracteres).
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Fecha de inicio de la práctica.
     */
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización de la práctica.
     */
    private LocalDate fechaFin;

    // --- FIN DE CAMPOS NUEVOS ---
}