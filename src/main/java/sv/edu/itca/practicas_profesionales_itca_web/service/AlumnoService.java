package sv.edu.itca.practicas_profesionales_itca_web.service;

import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import sv.edu.itca.practicas_profesionales_itca_web.repository.PropuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service // <-- Esta anotación es la que arregla el "No beans found"
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
        nuevaPropuesta.setFechaEnvio(LocalDate.now());

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
