//AlumnoController
package sv.edu.itca.practicas_profesionales_itca_web.controller;

import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.service.AlumnoService;
import sv.edu.itca.practicas_profesionales_itca_web.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumno") // Todos los endpoints aquí empiezan con /api/alumno
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final MyUserDetailsService userDetailsService; // Para obtener el objeto Usuario

    @Autowired
    public AlumnoController(AlumnoService alumnoService, MyUserDetailsService userDetailsService) {
        this.alumnoService = alumnoService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Endpoint para que el alumno vea su dashboard (historial y horas).
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getMiDashboard(Authentication authentication) {
        // (1) Obtenemos el usuario que está logueado
        Usuario alumno = getUsuarioDesdeAuth(authentication);

        // (2) Usamos los services para obtener los datos
        List<Propuesta> historial = alumnoService.getHistorialPropuestas(alumno);
        int horas = alumnoService.getHorasAcumuladas(alumno);

        // (3) Creamos una respuesta limpia para el frontend
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("historialPropuestas", historial);
        dashboardData.put("horasAcumuladas", horas);
        dashboardData.put("horasRequeridas", 320);

        return ResponseEntity.ok(dashboardData);
    }

    /**
     * Endpoint para que el alumno envíe una nueva propuesta.
     */
    @PostMapping("/propuestas")
    public ResponseEntity<Map<String, String>> submitPropuesta(
            @RequestBody Propuesta nuevaPropuesta,
            Authentication authentication
    ) {
        Usuario alumno = getUsuarioDesdeAuth(authentication);

        try {
            alumnoService.submitPropuesta(nuevaPropuesta, alumno);
            // Si todo sale bien, devolvemos un 201 Created
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Propuesta enviada a revisión."));
        } catch (IllegalStateException e) {
            // Si el servicio lanza una excepción (ej. "Ya tienes una activa")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Método helper para obtener nuestro objeto Usuario completo
     * desde el objeto Authentication de Spring Security.
     */
    private Usuario getUsuarioDesdeAuth(Authentication authentication) {
        // 1. Obtenemos el email (username) del usuario logueado
        String correo = authentication.getName();

        // 2. Usamos nuestro service para buscar el objeto Usuario completo
        return userDetailsService.getUsuarioByCorreo(correo);
    }
}
