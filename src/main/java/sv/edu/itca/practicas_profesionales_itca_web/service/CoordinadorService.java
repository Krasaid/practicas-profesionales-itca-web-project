package sv.edu.itca.practicas_profesionales_itca_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.edu.itca.practicas_profesionales_itca_web.model.Area;
import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.repository.PropuestaRepository;

import java.util.List;

@Service
public class CoordinadorService {

    @Autowired
    private PropuestaRepository propuestaRepo;

    public List<Propuesta> getPropuestasPorArea(Area area) {
        return propuestaRepo.findByAlumno_Area(area);
    }

    public Propuesta updateEstadoPropuesta(Long propuestaId, EstadoPropuesta nuevoEstado, String explicacion, Area areaCoordinador) {
        Propuesta p = propuestaRepo.findById(propuestaId)
                .orElseThrow(() -> new IllegalStateException("Propuesta no encontrada"));

     // Verificación de seguridad: El coord. solo puede editar propuestas de su área [cite: 28]
        if (p.getAlumno().getArea() != areaCoordinador) {
            throw new SecurityException("No tiene permisos para editar esta propuesta.");
        }

      // Regla de negocio: Explicación obligatoria si es Denegado [cite: 35]
        if (nuevoEstado == EstadoPropuesta.DENEGADO && (explicacion == null || explicacion.isBlank())) {
            throw new IllegalStateException("La explicación es obligatoria para denegar.");
        }

        p.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoPropuesta.DENEGADO) {
            p.setExplicacionRechazo(explicacion);
        } else {
            p.setExplicacionRechazo(null); // Limpia la explicación si se aprueba
        }
        return propuestaRepo.save(p);
    }
}