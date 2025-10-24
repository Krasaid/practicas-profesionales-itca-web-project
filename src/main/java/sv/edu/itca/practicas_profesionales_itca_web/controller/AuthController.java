package sv.edu.itca.practicas_profesionales_itca_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth") // Endpoint de autenticación
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para verificar las credenciales (Basic Auth) y devolver
     * los detalles del usuario (sin la contraseña).
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreoInstitucional(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        // Creamos una respuesta segura (sin el password)
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("email", usuario.getCorreoInstitucional());
        userDetails.put("rol", usuario.getRol().name());
        userDetails.put("nombre", usuario.getNombre());
        userDetails.put("apellido", usuario.getApellido());
        userDetails.put("area", usuario.getArea());

        return ResponseEntity.ok(userDetails);
    }
}