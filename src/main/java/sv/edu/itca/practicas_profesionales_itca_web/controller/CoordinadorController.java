package sv.edu.itca.practicas_profesionales_itca_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sv.edu.itca.practicas_profesionales_itca_web.dto.UpdateEstadoRequest;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;
import sv.edu.itca.practicas_profesionales_itca_web.service.CoordinadorService;

import java.util.List;

@RestController
@RequestMapping("/api/coordinador")
public class CoordinadorController {

    @Autowired
    private CoordinadorService coordinadorService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByCorreoInstitucional(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }
    /**
     * Baby, me he da'o cuenta que te amo
     * Que no sé lo que hago sin ti
     * Yo sé que unos cuantos te hablaron
     * Pero me prefieres a mí, a mí
     *
     * Más de media vida solo
     * Esperando el momento, que salgan mis planes
     * Pero creo que lo dejo todo
     * Esa boca me tiene como envuelto en diamantes
     */

    @GetMapping("/propuestas")
    public ResponseEntity<List<Propuesta>> getPropuestas(Authentication authentication) {
        Usuario coordinador = getUsuarioFromAuth(authentication);
        List<Propuesta> propuestas = coordinadorService.getPropuestasPorArea(coordinador.getArea());
        return ResponseEntity.ok(propuestas);
    }

    // ¡ESTE ES EL ENDPOINT QUE TE DABA 404!
    @PutMapping("/propuestas/{id}/estado")
    public ResponseEntity<Propuesta> updateEstado(
            @PathVariable Long id,
            @RequestBody UpdateEstadoRequest request,
            Authentication authentication) {

        Usuario coordinador = getUsuarioFromAuth(authentication); // Asegura que el que edita es un coord.

        try {
            Propuesta actualizada = coordinadorService.updateEstadoPropuesta(
                    id,
                    request.getNuevoEstado(),
                    request.getExplicacion(),
                    coordinador.getArea() // Pasa el área para seguridad
            );
            return ResponseEntity.ok(actualizada);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // No es de su área
        }
    }
}