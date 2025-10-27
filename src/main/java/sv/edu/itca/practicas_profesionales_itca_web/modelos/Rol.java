package sv.edu.itca.practicas_profesionales_itca_web.modelos;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Double id;

    @Column(length = 50, unique = true)
    private String rol; // Los roles identificados: "estudiante" y "coordinador"

}
