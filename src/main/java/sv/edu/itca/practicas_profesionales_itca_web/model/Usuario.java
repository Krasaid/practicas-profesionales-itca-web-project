package sv.edu.itca.practicas_profesionales_itca_web.model;

import jakarta.persistence.*;
import lombok.Data; // <-- ¡Importante!
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa a un Usuario del sistema.
 * Esta clase se mapea a la tabla "usuarios" en la base de datos.
 * Utiliza Lombok (@Data) para generar getters, setters y otros métodos comunes.
 */
@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único del usuario.
     * Es la clave primaria (PK) y se genera automáticamente por la base de datos (auto-incremental).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Correo electrónico institucional del usuario.
     * Debe ser único y no puede ser nulo.
     */
    @Column(unique = true, nullable = false)
    private String correoInstitucional;

    /**
     * Contraseña del usuario.
     * Se almacena hasheada. La lógica de hasheo es gestionada
     * por Spring Security antes de persistir la entidad.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario en el sistema (ej. ALUMNO, DOCENTE, ADMIN).
     * Se almacena como texto (STRING) en la base de datos gracias a EnumType. STRING.
     */
    @Enumerated(EnumType.STRING)
    private Rol rol;

    /**
     * Área a la que pertenece el usuario (ej. DESARROLLO, MANTENIMIENTO).
     * Se almacena como texto (STRING).
     */
    @Enumerated(EnumType.STRING)
    private Area area;

    /**
     * Lista de propuestas asociadas a este usuario (en caso de ser un alumno).
     * Es una relación "Uno a Muchos": Un Usuario (alumno) puede tener muchas Propuestas.
     * 'mappedBy = "alumno"' indica que la entidad Propuesta gestiona la clave foránea.
     */
    @OneToMany(mappedBy = "alumno")
    private List<Propuesta> propuestas;
}