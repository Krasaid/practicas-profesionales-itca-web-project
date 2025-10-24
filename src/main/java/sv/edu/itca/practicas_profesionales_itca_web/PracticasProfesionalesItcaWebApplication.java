package sv.edu.itca.practicas_profesionales_itca_web;

// --- Imports que necesitas ---
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

// (Asegúrate que estos imports apunten a tus paquetes correctos)
import sv.edu.itca.practicas_profesionales_itca_web.model.Area;
import sv.edu.itca.practicas_profesionales_itca_web.model.Rol;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;

@SpringBootApplication
public class PracticasProfesionalesItcaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticasProfesionalesItcaWebApplication.class, args);
    }

    // --- PEGA EL BEAN DEL SEMBRADOR AQUÍ ---
    /**
     * Bean para "sembrar" (seed) la base de datos con usuarios de prueba.
     * Solo se ejecutará si el repositorio de usuarios está vacío.
     */
    @Bean
    CommandLineRunner commandLineRunner(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Solo crear usuarios si la BD está vacía
            if (usuarioRepository.count() == 0) {

                // 1. Crear Alumno de Prueba
                Usuario alumno = new Usuario();
                alumno.setCorreoInstitucional("alumno@test.com");
                alumno.setPassword(passwordEncoder.encode("alumno123")); // ¡Importante! Encriptar
                alumno.setRol(Rol.ALUMNO);
                alumno.setArea(Area.SOFTWARE); // Asumiendo que existe, ajusta si es necesario
                usuarioRepository.save(alumno);

                // 2. Crear Coordinador de Prueba
                Usuario coordinador = new Usuario();
                coordinador.setCorreoInstitucional("coordinador@test.com");
                coordinador.setPassword(passwordEncoder.encode("coord123")); // ¡Importante! Encriptar
                coordinador.setRol(Rol.COORDINADOR);
                coordinador.setArea(Area.SOFTWARE); // Asumiendo que existe
                usuarioRepository.save(coordinador);

                System.out.println(">>> ¡Usuarios de prueba (alumno@test.com y coordinador@test.com) creados! <<<");
            }
        };
    }
}