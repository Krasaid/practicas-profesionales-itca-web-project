package sv.edu.itca.practicas_profesionales_itca_web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "propuestas")
public class Propuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchas propuestas pertenecen a un alumno
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    @Enumerated(EnumType.STRING)
    private EstadoPropuesta estado; // [cite: 34, 35, 37]

    private String empresa;
    private LocalDate fechaEnvio;
    private int horasPropuestas;

    // Para la explicación obligatoria en caso de rechazo [cite: 35]
    private String explicacionRechazo;
}
