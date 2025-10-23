package sv.edu.itca.practicas_profesionales_itca_web.modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Versión mínima del modelo para poder implementar el inicio de sesión
    // luego añaden el resto que haga falta
    private Double id;

    @Column(unique = true, nullable = false)
    private String carnet;

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false)
    private String nombre;


    // La autenticación con Spring Security requiere que se cree un modelo
    // aparte para los roles y luego se relacione con el modelo de usuario
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Rol> roles;
}
