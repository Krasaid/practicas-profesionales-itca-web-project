package sv.edu.itca.practicas_profesionales_itca_web.controller;

import sv.edu.itca.practicas_profesionales_itca_web.dto.UpdateEstadoRequest; // DTO que contendrá nuevoEstado y explicacion
import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.service.CoordinadorService;
import sv.edu.itca.practicas_profesionales_itca_web.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/coordinador")
public class CoordinadorController {

    private final CoordinadorService coordinadorService;
    private final MyUserDetailsService userDetailsService;

    @Autowired
    public CoordinadorController(CoordinadorService coordinadorService, MyUserDetailsService userDetailsService) {
        this.coordinadorService = coordinadorService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Endpoint para ver todas las propuestas (con filtros).
     */
    @GetMapping("/propuestas")
    public ResponseEntity<List<Propuesta>> getPropuestas(
            Authentication authentication,
            @RequestParam(required = false) String estado, // Filtro por estado
            @RequestParam(required = false) String empresa // Filtro por empresa
    ) {
        Usuario coordinador = getUsuarioDesdeAuth(authentication);

        List<Propuesta> propuestas = coordinadorService.getPropuestasPorArea(
                coordinador.getArea(), // Filtra automáticamente por el área del coordinador
                estado,
                empresa
        );
        return ResponseEntity.ok(propuestas);
    }

    /**
     * Endpoint para aprobar o denegar una propuesta.
     */
    @PutMapping("/propuestas/{id}/estado")
    public ResponseEntity<Propuesta> updateEstado(
            @PathVariable Long id,
            @RequestBody UpdateEstadoRequest request
    ) {
        try {
            Propuesta actualizada = coordinadorService.updateEstadoPropuesta(
                    id,
                    request.getNuevoEstado(),
                    request.getExplicacion()
            );
            return ResponseEntity.ok(actualizada);
        } catch (IllegalStateException e) {
            // Si falta la explicación al denegar
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            // Si no se encuentra la propuesta
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Propuesta no encontrada");
        }
    }

    /**
     * Método helper para obtener el usuario autenticado.
     */
    private Usuario getUsuarioDesdeAuth(Authentication authentication) {
        String correo = authentication.getName();
        return userDetailsService.getUsuarioByCorreo(correo);
    }
}
