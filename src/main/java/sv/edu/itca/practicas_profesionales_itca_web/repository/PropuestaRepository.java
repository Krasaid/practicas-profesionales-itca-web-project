package sv.edu.itca.practicas_profesionales_itca_web.repository;

import sv.edu.itca.practicas_profesionales_itca_web.model.Area;
import sv.edu.itca.practicas_profesionales_itca_web.model.EstadoPropuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Propuesta;
import sv.edu.itca.practicas_profesionales_itca_web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropuestaRepository extends JpaRepository<Propuesta, Long> {

    // Para el dashboard del alumno (historial)
    List<Propuesta> findByAlumno(Usuario alumno);

    // Para el dashboard del coordinador (filtrar por área)
    List<Propuesta> findByAlumno_Area(Area area);

    // Para la regla "Solo una propuesta activa en revisión por alumno"
    boolean existsByAlumnoAndEstado(Usuario alumno, EstadoPropuesta estado);

    // (Opcional, pero más eficiente para calcular horas)
    // Suma las horas de todas las propuestas APROBADAS de un alumno
    @Query("SELECT SUM(p.horasPropuestas) FROM Propuesta p WHERE p.alumno = :alumno AND p.estado = 'APROBADO'")
    Optional<Long> sumHorasAprobadasByAlumno(@Param("alumno") Usuario alumno);
}
