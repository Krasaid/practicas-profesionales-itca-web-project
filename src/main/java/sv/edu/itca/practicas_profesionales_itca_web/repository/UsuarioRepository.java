//UsuarioRepository
package sv.edu.itca.practicas_profesionales_itca_web.repository;

import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA entiende este método por el nombre.
    // Lo usará Spring Security para buscar al usuario por su correo al hacer login [cite: 16, 29].
    Optional<Usuario> findByCorreoInstitucional(String correo);
}
