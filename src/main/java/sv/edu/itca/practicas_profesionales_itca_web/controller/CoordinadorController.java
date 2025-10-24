package sv.edu.itca.practicas_profesionales_itca_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sv.edu.itca.practicas_profesionales_itca_web.dto.CrearUsuarioRequest;
import sv.edu.itca.practicas_profesionales_itca_web.dto.UpdateEstadoRequest;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;
import sv.edu.itca.practicas_profesionales_itca_web.service.CoordinadorService;
import sv.edu.itca.practicas_profesionales_itca_web.service.UsuarioService;

// --- ¡FIX 1: Imports Limpios! ---
import java.util.List;
import java.util.Map; // <-- ¡AÑADIDO! Faltaba este import
// (Quitamos los imports duplicados)
// --- FIN FIX 1 ---

@RestController
@RequestMapping("/api/coordinador")
public class CoordinadorController {

    @Autowired
    private CoordinadorService coordinadorService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene el usuario autenticado a partir del token.
     * (Esta es la ÚNICA definición del método)
     */
    private Usuario getUsuarioFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByCorreoInstitucional(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario no encontrado"
                ));
    }

    /**
     * Devuelve las propuestas asignadas al área del coordinador autenticado.
     */
    @GetMapping("/propuestas")
    public ResponseEntity<List<Propuesta>> getPropuestas(Authentication authentication) {
        Usuario coordinador = getUsuarioFromAuth(authentication);
        List<Propuesta> propuestas = coordinadorService.getPropuestasPorArea(coordinador.getArea());
        return ResponseEntity.ok(propuestas);
    }

    /**
     * Actualiza el estado de una propuesta (HU-9).
     */
    @PutMapping("/propuestas/{id}/estado")
    public ResponseEntity<Propuesta> updateEstado(
            @PathVariable Long id,
            @RequestBody UpdateEstadoRequest request,
            Authentication authentication
    ) {
        Usuario coordinador = getUsuarioFromAuth(authentication);

        try {
            Propuesta actualizada = coordinadorService.updateEstadoPropuesta(
                    id,
                    request.getNuevoEstado(),
                    request.getExplicacion(),
                    coordinador.getArea()
            );
            return ResponseEntity.ok(actualizada);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- ¡FIX 2: Quitamos el bloque comentado! ---
    // (Ya no necesitamos la versión antigua y comentada)

    // --- ¡NUEVO ENDPOINT (HU-12)! ---
    @PostMapping("/alumnos") // Endpoint: POST /api/coordinador/alumnos
    public ResponseEntity<?> crearNuevoAlumno(@RequestBody CrearUsuarioRequest request) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(request);
            // (Devolvemos un Map, ahora SÍ está importado)
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario creado con ID: " + nuevoUsuario.getId()));
        } catch (IllegalStateException e) {
            // (Manejo de error si el correo ya existe)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }
    // --- FIN NUEVO ENDPOINT ---


    // --- ¡FIX 3: BORRAMOS el método duplicado getUsuarioFromAuth() de aquí! ---

}