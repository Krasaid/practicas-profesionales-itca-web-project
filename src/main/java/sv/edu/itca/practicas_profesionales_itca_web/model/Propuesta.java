package sv.edu.itca.practicas_profesionales_itca_web.model; // (O tu paquete)

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data; // <-- Asegúrate que @Data esté importado
import java.time.LocalDate;

@Data // <-- ¡Asegúrate que @Data esté aquí!
@Entity
@Table(name = "propuestas")
public class Propuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    @Enumerated(EnumType.STRING)
    private EstadoPropuesta estado;

    @Column(nullable = false)
    private String empresa;

    private LocalDate fechaEnvio;

    @Column(nullable = false)
    private int horasPropuestas;

    private String explicacionRechazo;

    // --- ¡ASEGÚRATE QUE ESTOS 3 CAMPOS ESTÉN AQUÍ! ---
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    // --- FIN DE CAMPOS ---
}