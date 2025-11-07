//CoordinadorService
package sv.edu.itca.practicas_profesionales_itca_web.service;

import sv.edu.itca.practicas_profesionales_itca_web.model.Area;
import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.repository.PropuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
// Consulta dinamica en el servicio (agregado)
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinadorService {

    private final PropuestaRepository propuestaRepo;

    @Autowired
    public CoordinadorService(PropuestaRepository propuestaRepo) {
        this.propuestaRepo = propuestaRepo;
    }

    //obtener propuestas con filtros ORIGINAL
    /*
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
        */
    //Metodo para obtener propuesta con filtros actulizado
    public List <Propuesta> getPropuestasConFiltros(Area area,EstadoPropuesta estado, String empresa, Long alumnoId){
        // 1. Empezar con la especificación base (traer todo lo del área del coord.)
        Specification<Propuesta> spec = (root, query, cb) -> cb.equal(root.get("alumno").get("area"), area);

        // 2. Añadir filtros opcionales
        if (estado != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("estado"), estado));
        }
        if (empresa != null && !empresa.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("empresa"), "%" + empresa + "%"));
        }
        if (alumnoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("alumno").get("id"), alumnoId));
        }
        // 3. Ejecutar la consulta
        return propuestaRepo.findAll(spec);
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
