package sv.edu.itca.practicas_profesionales_itca_web.config;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import sv.edu.itca.practicas_profesionales_itca_web.model.Rol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // (1) Bean para encriptar contraseñas
    // Spring lo usará automáticamente para comparar contraseñas en el login
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // (2) Bean principal que configura las reglas de acceso (el "firewall")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque usaremos una API REST (normalmente con tokens, no cookies de sesión)
                .csrf(AbstractHttpConfigurer::disable)

                // Definimos las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso a todos para un futuro endpoint de login
                        .requestMatchers("/api/auth/**").permitAll()

                        // Protege los endpoints de ALUMNO
                        .requestMatchers("/api/alumno/**").hasAuthority(Rol.ALUMNO.name())

                        // Protege los endpoints de COORDINADOR
                        .requestMatchers("/api/coordinador/**").hasAuthority(Rol.COORDINADOR.name())

                        // Cualquier otra petición debe estar autenticada
                        .anyRequest().authenticated()
                )

                // Le decimos a Spring Security que no maneje sesiones (modo "stateless" para API REST)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Habilita la configuración básica de login HTTP
        // Esto te dará un formulario de login por defecto de Spring mientras construyes el frontend
        http.httpBasic(basic -> {});

        return http.build();
    }
}
