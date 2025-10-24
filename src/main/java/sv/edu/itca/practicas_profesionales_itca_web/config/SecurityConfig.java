package sv.edu.itca.practicas_profesionales_itca_web.config;

import sv.edu.itca.practicas_profesionales_itca_web.model.Rol;
import sv.edu.itca.practicas_profesionales_itca_web.service.MyUserDetailsService; // <-- ¡IMPORTA ESTO!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // <-- ¡IMPORTA ESTO!
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- ¡FIX 1: INYECTAR EL SERVICIO! ---
    private final MyUserDetailsService myUserDetailsService;

    public SecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }
    // --- FIN FIX 1 ---

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- ¡FIX 2: CREAR EL PROVEEDOR DE AUTENTICACIÓN! ---
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService); // <-- Usa tu servicio
        authProvider.setPasswordEncoder(passwordEncoder()); // <-- Usa tu encriptador
        return authProvider;
    }
    // --- FIN FIX 2 ---

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // --- ¡LÍNEA AÑADIDA! ---
                        // Permite que cualquier usuario autenticado consulte su identidad
                        .requestMatchers("/api/auth/me").authenticated()
                        // --- FIN DE LÍNEA ---

                        // --- RUTAS POR ROL ---
                        .requestMatchers("/api/alumno/**").hasAuthority(Rol.ALUMNO.name())
                        .requestMatchers("/api/coordinador/**").hasAuthority(Rol.COORDINADOR.name())

                        // --- CUALQUIER OTRA RUTA REQUIERE AUTENTICACIÓN ---
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {})
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // --- ¡FIX 3: APLICAR EL PROVEEDOR! ---
        http.authenticationProvider(authenticationProvider());
        // --- FIN FIX 3 ---

        return http.build();
    }

    // (El Bean de CORS sigue igual)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
