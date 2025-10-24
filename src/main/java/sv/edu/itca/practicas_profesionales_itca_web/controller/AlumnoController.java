package sv.edu.itca.practicas_profesionales_itca_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;
import sv.edu.itca.practicas_profesionales_itca_web.service.AlumnoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private UsuarioRepository usuarioRepository; // Lo necesitamos para buscar al usuario

    // Helper para obtener el Usuario desde la autenticación
    private Usuario getUsuarioFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByCorreoInstitucional(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getMiDashboard(Authentication authentication) {
        Usuario alumno = getUsuarioFromAuth(authentication);
        List<Propuesta> historial = alumnoService.getHistorialPropuestas(alumno);
        int horas = alumnoService.getHorasAcumuladas(alumno);

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("historialPropuestas", historial);
        dashboardData.put("horasAcumuladas", horas);
        dashboardData.put("horasRequeridas", 320); // [cite: 46]

        return ResponseEntity.ok(dashboardData);
    }

    @PostMapping("/propuestas")
    public ResponseEntity<Map<String, String>> submitPropuesta(@RequestBody Propuesta nuevaPropuesta, Authentication authentication) {
        Usuario alumno = getUsuarioFromAuth(authentication);
        try {
            alumnoService.submitPropuesta(nuevaPropuesta, alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Propuesta enviada a revisión."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}