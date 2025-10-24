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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Clase de configuración principal para Spring Security.
 * Define la seguridad de la API, incluyendo CORS, autorización de endpoints,
 * gestión de sesiones y el codificador de contraseñas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define el Bean para el codificador de contraseñas.
     * Utiliza BCrypt, el estándar de oro para el hasheo de contraseñas.
     * Este Bean será inyectado en servicios (ej. UsuarioService) para
     * hashear las contraseñas antes de guardarlas en la BD.
     *
     * @return una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define la cadena de filtros de seguridad principal que se aplica
     * a todas las solicitudes HTTP.
     *
     * @param http el objeto HttpSecurity para configurar.
     * @return la SecurityFilterChain construida.
     * @throws Exception si ocurre un error de configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuración de CORS
                // Habilita CORS y utiliza el bean 'corsConfigurationSource' definido abajo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Desactivación de CSRF
                // Se deshabilita CSRF porque la API es 'stateless' (sin sesiones)
                // y usará tokens (ej. JWT) que no son vulnerables a ataques CSRF.
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Reglas de Autorización de Solicitudes
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso público a los endpoints de autenticación
                        .requestMatchers("/api/auth/**").permitAll()
                        // Solo ALUMNO puede acceder a las rutas de alumno
                        .requestMatchers("/api/alumno/**").hasAuthority(Rol.ALUMNO.name())
                        // Solo COORDINADOR puede acceder a las rutas de coordinador
                        .requestMatchers("/api/coordinador/**").hasAuthority(Rol.COORDINADOR.name())
                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )

                // 4. Gestión de Sesión
                // Se configura la política como STATELESS (sin estado).
                // Spring Security no creará ni usará HttpSessions (cookies).
                // Cada petición debe autenticarse por sí misma (ej. con un token Bearer).
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Autenticación (Configuración básica)
                // NOTA: Para un sistema stateless real, aquí faltaría el filtro
                // de JWT (ej.addFilterBefore(jwtFilter, ...))
                .httpBasic(basic -> {}); // Habilita HTTP Basic como fallback (o se puede deshabilitar)

        return http.build();
    }

    /**
     * Define el Bean con la configuración de CORS (Cross-Origin Resource Sharing).
     * Esto es crucial para permitir que el frontend (ej. en localhost:5173)
     * pueda hacer peticiones a esta API (ej. en localhost:8080).
     *
     * @return la fuente de configuración de CORS.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Define los orígenes permitidos (¡solo el frontend de desarrollo por ahora!)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Define los métodos HTTP permitidos (GET, POST, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permite todos los encabezados (ej. 'Content-Type', 'Authorization')
        configuration.setAllowedHeaders(List.of("*"));

        // Permite que el navegador envíe credenciales (cookies, tokens)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración 'configuration' a todas las rutas bajo "/api/**"
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}