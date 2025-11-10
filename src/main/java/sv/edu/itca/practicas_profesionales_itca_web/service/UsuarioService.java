package sv.edu.itca.practicas_profesionales_itca_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sv.edu.itca.practicas_profesionales_itca_web.dto.CrearUsuarioRequest;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Para encriptar la contraseña

    // La contraseña temporal por defecto
    private final String TEMPORAL_PASSWORD = "itca2025";

    public Usuario crearUsuario(CrearUsuarioRequest request) {

        // (1) Validación: No permitir correos duplicados
        if (usuarioRepository.findByCorreoInstitucional(request.getCorreoInstitucional()).isPresent()) {
            throw new IllegalStateException("El correo institucional '" + request.getCorreoInstitucional() + "' ya está en uso.");
        }

        // (2) Crear el nuevo objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellido(request.getApellido());
        nuevoUsuario.setCorreoInstitucional(request.getCorreoInstitucional());
        nuevoUsuario.setRol(request.getRol());
        nuevoUsuario.setArea(request.getArea());

        // (3) Asignar y encriptar la contraseña temporal
        nuevoUsuario.setPassword(passwordEncoder.encode(TEMPORAL_PASSWORD));

        // (4) Guardar en la base de datos
        return usuarioRepository.save(nuevoUsuario);
    }
}