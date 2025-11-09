//CoordinadorController

package sv.edu.itca.practicas_profesionales_itca_web.controller;


import sv.edu.itca.practicas_profesionales_itca_web.dto.UpdateEstadoRequest; // DTO que contendrá nuevoEstado y explicación
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
import sv.edu.itca.practicas_profesionales_itca_web.dto.UpdateEstadoRequest;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.UsuarioRepository;
import sv.edu.itca.practicas_profesionales_itca_web.service.CoordinadorService;

import java.util.List;
/**
 * Controlador REST para las funcionalidades del rol COORDINADOR.
 * Protegido bajo la ruta base "/api/coordinador" por Spring Security.
 */
@RestController
@RequestMapping("/api/coordinador")
public class CoordinadorController {

    private final CoordinadorService coordinadorService;
    private final MyUserDetailsService userDetailsService;
    /**
     * Inyección de dependencias vía constructor.
     *
     * @param coordinadorService Servicio con la lógica de negocio del coordinador.
     * @param userDetailsService Servicio para cargar datos del usuario autenticado.
     */
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
     * Endpoint para obtener la lista de propuestas con filtros dinámicos.
     * Filtra automáticamente por el área del coordinador logueado.
     *
     * @param authentication Objeto de seguridad de Spring (inyectado).
     * @param estado         (Opcional) Filtra por estado (ej. "APROBADO", "EN_REVISION").
     * @param empresa        (Opcional) Filtra por nombre de empresa (búsqueda 'like').
     * @param alumnoId       (Opcional) Filtra por el ID de un alumno específico.
     * @return ResponseEntity con la lista de propuestas (HTTP 200).
     */
    @GetMapping("/propuestas")
    public ResponseEntity<List<Propuesta>> getPropuestas(
            Authentication authentication,
            @RequestParam(required = false) String estado, // Filtro por estado
            @RequestParam(required = false) String empresa,// Filtro por empresa
            @RequestParam(required = false) Long alumnoId
    ) {
        Usuario coordinador = getUsuarioDesdeAuth(authentication);
    EstadoPropuesta estadoEnum = null;
        if (estado != null && !estado.isBlank()) {
            try {
                // Intenta convertir el texto (ej. "APROBADO") al Enum
                estadoEnum = EstadoPropuesta.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si el frontend envía un estado inválido (ej. "APROBADO123"),
                // se lanza una excepción. La capturamos y devolvemos un error 400.
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de 'estado' inválido: " + estado);
            }
        }
        List<Propuesta> propuestas = coordinadorService.getPropuestasConFiltros(
                coordinador.getArea(), // (Area) Filtra automáticamente por el área del coordinador
                estadoEnum,            // (EstadoPropuesta) El Enum convertido (o null)
                empresa,               // (String) El nombre de la empresa (o null)
                alumnoId               // (Long) El ID del alumno (o null)
        );

        return ResponseEntity.ok(propuestas);
        /*
        List<Propuesta> propuestas = coordinadorService.getPropuestasConFiltros(coordinador.getArea(), // Filtra automáticamente por el área del coordinador
                estado,
                empresa,

        );

        return ResponseEntity.ok(propuestas);

    }
 */

    }
    /**
     * Endpoint para aprobar o denegar una propuesta.
     */
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
            // Sí falta la explicación al denegar
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            // Si no se encuentra la propuesta
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Propuesta no encontrada");
        }
    }
}