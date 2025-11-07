package sv.edu.itca.practicas_profesionales_itca_web.controller; // O el paquete.advice si lo creaste

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

/**
 * Este Controller Advice añade atributos globales al Model
 * para todas las vistas renderizadas por Thymeleaf.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Añade la URI de la solicitud actual al modelo como "requestURI".
     * Esto es necesario porque el objeto '#request' ya no está disponible
     * por defecto en Thymeleaf con Spring Security 6+.
     */
    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        // Pasamos la URI (ej. "/alumno/dashboard") al modelo
        model.addAttribute("requestURI", request.getRequestURI());
    }
}