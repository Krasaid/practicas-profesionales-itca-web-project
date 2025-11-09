//AlumnoService
package sv.edu.itca.practicas_profesionales_itca_web.service;

import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.PropuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AlumnoService {

    private final PropuestaRepository propuestaRepo;
    // Definimos el límite de horas requerido
    private static final int HORAS_REQUERIDAS = 320; // [cite: 46]

    @Autowired
    public AlumnoService(PropuestaRepository propuestaRepo) {
        this.propuestaRepo = propuestaRepo;
    }

    /**
     * Procesa el envío de una nueva propuesta, aplicando las reglas de negocio.
     */
    public void submitPropuesta(Propuesta nuevaPropuesta, Usuario alumno) {

        // --- 1. (HU-03) Validar si ya tiene una activa ---
        boolean tieneActiva = propuestaRepo.existsByAlumnoAndEstado(alumno, EstadoPropuesta.EN_REVISION);
        if (tieneActiva) {
            throw new IllegalStateException("Ya tienes una propuesta en revisión."); //
        }

        // --- 2. (HU-05) Validar horas ---
        int horasAcumuladas = getHorasAcumuladas(alumno);

        // 2a. Si ya tiene 320 o más, no puede enviar más.
        if (horasAcumuladas >= HORAS_REQUERIDAS) {
            throw new IllegalStateException("Felicidades, ya completaste tus 320 horas."); //
        }

        // 2b. (Fix de lógica para [image_5557bd.png])
        // Si aún no tiene 320, pero la NUEVA propuesta es excesiva
        int horasNuevas = nuevaPropuesta.getHorasPropuestas();
        int horasFaltantes = HORAS_REQUERIDAS - horasAcumuladas;

        // Permitimos un margen (ej. 80h) sobre lo faltante, pero no 3000h
        int limiteRazonable = horasFaltantes + 80;

        if (horasNuevas > limiteRazonable && horasNuevas > HORAS_REQUERIDAS) {
            throw new IllegalStateException(
                    "El máximo de horas para esta propuesta es (" + limiteRazonable + "). Ya tienes " + horasAcumuladas + "h y te faltan " + horasFaltantes + "h."
            );
        }

        // --- 3. Preparar y Guardar la Propuesta ---
        nuevaPropuesta.setAlumno(alumno);
        nuevaPropuesta.setEstado(EstadoPropuesta.EN_REVISION); // [cite: 26, 37]
        nuevaPropuesta.setFechaEnvio(LocalDate.now()); // (Fix para [image_abdf87.png])

        // (Asignamos los campos del formulario de React)
        nuevaPropuesta.setDescripcion(nuevaPropuesta.getDescripcion());
        nuevaPropuesta.setFechaInicio(nuevaPropuesta.getFechaInicio());
        nuevaPropuesta.setFechaFin(nuevaPropuesta.getFechaFin());

        propuestaRepo.save(nuevaPropuesta);
    }

    /**
     * Obtiene el historial de propuestas para el dashboard del alumno. [cite: 59]
     */
    public List<Propuesta> getHistorialPropuestas(Usuario alumno) {
        return propuestaRepo.findByAlumno(alumno);
    }

    /**
     * Calcula el total de horas APROBADAS de un alumno. [cite: 60]
     */
    public int getHorasAcumuladas(Usuario alumno) {
        return propuestaRepo.sumHorasAprobadasByAlumno(alumno)
                .map(Long::intValue)
                .orElse(0); // Devuelve 0 si es null
    }
}