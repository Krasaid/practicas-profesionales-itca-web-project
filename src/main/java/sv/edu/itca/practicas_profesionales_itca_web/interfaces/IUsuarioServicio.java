package sv.edu.itca.practicas_profesionales_itca_web.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sv.edu.itca.practicas_profesionales_itca_web.modelos.Usuario;

public interface IUsuarioServicio extends UserDetailsService {

    // Asumiendo que el nuevo sistema se conecta al sistema existente, se realiza
    // en lugar de un login normal, una "activación de cuenta" en el sistema
    // dependiendo del status del estudiante
    public enum ActivationStatus {
        OK_CONTINUE,     // El carnet es válido y no existe, puede crear clave
        ALREADY_EXISTS,  // El carnet ya está en nuestra BD
        SSS_NOT_FOUND     // El carnet no pasó la validación del Servicio Social Estudiantil
    }

    // Verifica si el estudiante puede o no activar su cuenta en el sistema de Prácticas
    public ActivationStatus verificarActivacion (String carnet);

    // Crea y guarda un nuevo usuario con el rol de "estudiante" en la base de datos
    public Usuario registrarEstudiante(String carnet, String clave);

    // Función requerida por UserDetailsService de Spring Security para buscar por
    // nombre de usuario (en este caso, el carnet) y devolver los detalles del usuario
    @Override
    public UserDetails loadUserByUsername(String carnet) throws UsernameNotFoundException;


}
