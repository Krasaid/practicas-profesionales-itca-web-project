package sv.edu.itca.practicas_profesionales_itca_web.service; // (Asegúrate que el paquete sea el tuyo)

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

    @Autowired
    public AlumnoService(PropuestaRepository propuestaRepo) {
        this.propuestaRepo = propuestaRepo;
    }

    public void submitPropuesta(Propuesta nuevaPropuesta, Usuario alumno) {
        boolean tieneActiva = propuestaRepo.existsByAlumnoAndEstado(alumno, EstadoPropuesta.EN_REVISION);
        if (tieneActiva) {
            throw new IllegalStateException("Ya tienes una propuesta en revisión.");
        }
        int horasAcumuladas = getHorasAcumuladas(alumno);
        if (horasAcumuladas >= 320) {
            throw new IllegalStateException("Felicidades, ya completaste tus 320 horas.");
        }
        nuevaPropuesta.setAlumno(alumno);
        nuevaPropuesta.setEstado(EstadoPropuesta.EN_REVISION);
        nuevaPropuesta.setFechaEnvio(LocalDate.now()); // ¡Fix de fecha!
        // Añadimos los campos que faltaban del formulario de React
        nuevaPropuesta.setDescripcion(nuevaPropuesta.getDescripcion());
        nuevaPropuesta.setFechaInicio(nuevaPropuesta.getFechaInicio());
        nuevaPropuesta.setFechaFin(nuevaPropuesta.getFechaFin());

        propuestaRepo.save(nuevaPropuesta);
    }

    public List<Propuesta> getHistorialPropuestas(Usuario alumno) {
        return propuestaRepo.findByAlumno(alumno);
    }

    public int getHorasAcumuladas(Usuario alumno) {
        return propuestaRepo.sumHorasAprobadasByAlumno(alumno)
                .map(Long::intValue)
                .orElse(0);
    }
}