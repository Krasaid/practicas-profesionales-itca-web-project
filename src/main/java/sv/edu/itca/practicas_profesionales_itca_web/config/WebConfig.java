//WebConfig
package sv.edu.itca.practicas_profesionales_itca_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // <-- ¡LA ANOTACIÓN ES CRUCIAL!
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Para /api/
                        .allowedOrigins("http://localhost:5174") // Origen FE
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos
                        .allowedHeaders("*") // Headers (incluye Authorization)
                        .allowCredentials(true); // Credenciales
            }
        };
    }
}