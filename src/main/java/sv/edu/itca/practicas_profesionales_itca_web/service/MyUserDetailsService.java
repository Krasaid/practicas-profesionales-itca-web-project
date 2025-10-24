package sv.edu.itca.practicas_profesionales_itca_web.service;

import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    // (Buena práctica: Inyección por constructor)
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MyUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // --- MÉTODO 1: El que usa Spring Security ---
    @Override
    public UserDetails loadUserByUsername(String correoInstitucional) throws UsernameNotFoundException {

        // 1. Buscamos al usuario en nuestra BD
        Usuario usuario = usuarioRepository.findByCorreoInstitucional(correoInstitucional)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correoInstitucional));

        // 2. Creamos la "autoridad" (el rol)
        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().name());

        // 3. Devolvemos un objeto UserDetails que Spring Security entiende
        return new User(
                usuario.getCorreoInstitucional(),
                usuario.getPassword(),
                Collections.singleton(authority) // Una colección con un solo rol
        );
    }

    // --- MÉTODO 2: El que usan nuestros Controladores ---
    /**
     * Método helper para que los controladores puedan obtener el objeto Usuario completo.
     */
    public Usuario getUsuarioByCorreo(String correo) {
        return usuarioRepository.findByCorreoInstitucional(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
    }
}
