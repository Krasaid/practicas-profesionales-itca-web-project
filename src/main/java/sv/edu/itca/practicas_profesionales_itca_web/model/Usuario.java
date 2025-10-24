package sv.edu.itca.practicas_profesionales_itca_web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String correoInstitucional;

    @Column(nullable = false)
    private String password; // Spring Security se encargará de hashearla

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private Area area;

    // Un alumno tiene muchas propuestas
    @OneToMany(mappedBy = "alumno")
    private List<Propuesta> propuestas;
}
