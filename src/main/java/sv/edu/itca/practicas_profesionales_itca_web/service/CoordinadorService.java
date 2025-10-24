package sv.edu.itca.practicas_profesionales_itca_web.service;

import sv.edu.itca.practicas_profesionales_itca_web.model.Area;
import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.repository.PropuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoordinadorService {

    private final PropuestaRepository propuestaRepo;

    @Autowired
    public CoordinadorService(PropuestaRepository propuestaRepo) {
        this.propuestaRepo = propuestaRepo;
    }

    public List<Propuesta> getPropuestasPorArea(Area area, String filtroEstado, String filtroEmpresa) {

        List<Propuesta> propuestas = propuestaRepo.findByAlumno_Area(area);

        if (filtroEstado != null && !filtroEstado.isBlank()) {
            propuestas = propuestas.stream()
                    .filter(p -> p.getEstado().name().equalsIgnoreCase(filtroEstado))
                    .collect(Collectors.toList());
        }

        if (filtroEmpresa != null && !filtroEmpresa.isBlank()) {
            propuestas = propuestas.stream()
                    .filter(p -> p.getEmpresa().toLowerCase().contains(filtroEmpresa.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return propuestas;
    }

    public Propuesta updateEstadoPropuesta(Long propuestaId, EstadoPropuesta nuevoEstado, String explicacion) {

        Propuesta propuesta = propuestaRepo.findById(propuestaId)
                .orElseThrow(() -> new RuntimeException("Propuesta no encontrada"));

        if (nuevoEstado == EstadoPropuesta.DENEGADO) {
            if (explicacion == null || explicacion.isBlank()) {
                throw new IllegalStateException("La explicación es obligatoria para denegar una propuesta.");
            }
            propuesta.setExplicacionRechazo(explicacion);
        } else {
            propuesta.setExplicacionRechazo(null);
        }

        propuesta.setEstado(nuevoEstado);
        return propuestaRepo.save(propuesta);
    }
}
