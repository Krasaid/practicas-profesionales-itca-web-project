package sv.edu.itca.practicas_profesionales_itca_web.model;

import jakarta.persistence.*;
import lombok.Data; // <-- ¡Importante!
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data // <-- ¡La anotación clave!
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String correoInstitucional;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private Area area;

    @JsonManagedReference
    @OneToMany(mappedBy = "alumno")
    private List<Propuesta> propuestas;

    // --- NUEVO (Basado en el mockup del frontend) ---
    private String nombre;
    private String apellido;
}