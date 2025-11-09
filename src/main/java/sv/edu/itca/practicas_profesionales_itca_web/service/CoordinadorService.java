//CoordinadorService
package sv.edu.itca.practicas_profesionales_itca_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    private PropuestaRepository propuestaRepo;

    //obtener propuestas con filtros ORIGINAL
    /*
    public List<Propuesta> getPropuestasPorArea(Area area, String filtroEstado, String filtroEmpresa) {

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
            p.setExplicacionRechazo(explicacion);
        } else {
            p.setExplicacionRechazo(null); // Limpia la explicación si se aprueba
        }
        return propuestaRepo.save(p);
    }
}