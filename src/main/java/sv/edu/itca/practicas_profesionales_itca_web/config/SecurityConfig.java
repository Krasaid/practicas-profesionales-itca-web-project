package sv.edu.itca.practicas_profesionales_itca_web.config; // Asegúrate que el paquete sea el tuyo

import sv.edu.itca.practicas_profesionales_itca_web.model.Rol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- ¡Vuelve!
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // (1) ¡Activamos CORS! (Lo necesita React)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // (2) Desactivamos CSRF (innecesario para una API REST stateless)
                .csrf(csrf -> csrf.disable())

                // (3) Reglas de autorización para los endpoints de la API
                .authorizeHttpRequests(auth -> auth
                        // (Permitimos /api/auth/** si tuvieras registro, etc.)
                        // .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/alumno/**").hasAuthority(Rol.ALUMNO.name())
                        .requestMatchers("/api/coordinador/**").hasAuthority(Rol.COORDINADOR.name())
                        .anyRequest().authenticated()
                )

                // (4) ¡Volvemos a httpBasic()! Spring Security manejará el header 'Authorization: Basic ...'
                .httpBasic(basic -> {})

                // (5) ¡Volvemos a STATELESS! Spring no creará sesiones.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    // (6) El Bean de CORS que permite la conexión desde React (localhost:5174)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ¡Importante! Esta debe ser la URL de tu frontend (Vite/React)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5174"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // Aplica solo a /api/
        return source;
    }
}