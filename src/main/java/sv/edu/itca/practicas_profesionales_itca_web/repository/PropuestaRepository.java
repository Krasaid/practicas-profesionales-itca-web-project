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

/**
 * Repositorio de Spring Data JPA para la entidad {@link Propuesta}.
 * Esta interfaz proporciona automáticamente métodos CRUD (Create, Read, Update, Delete)
 * al extender JpaRepository, además de las consultas personalizadas definidas.
 */
@Repository
public interface PropuestaRepository extends JpaRepository<Propuesta, Long> {

    /**
     * Busca todas las propuestas asociadas a un alumno específico.
     * Utilizado para el historial del dashboard del alumno.
     *
     * @param alumno El usuario (alumno) cuyas propuestas se quieren obtener.
     * @return Una lista de {@link Propuesta}s pertenecientes al alumno.
     */
    List<Propuesta> findByAlumno(Usuario alumno);

    /**
     * Busca todas las propuestas de los alumnos que pertenecen a un área específica.
     * Utiliza la navegación de propiedades (Propuesta -> Alumno -> Area).
     *
     * @param area Él {@link Area} por la cual filtrar.
     * @return Una lista de {@link Propuesta}s de alumnos de esa área.
     */
    List<Propuesta> findByAlumno_Area(Area area);

    /**
     * Verifica si existe al menos una propuesta para un alumno específico en un estado determinado.
     * Es más eficiente que un 'find' si solo se necesita saber si existe.
     *
     * @param alumno El usuario (alumno) a verificar.
     * @param estado El {@link EstadoPropuesta} a verificar (ej. EN_REVISION).
     * @return true si existe al menos una coincidencia, false en caso contrario.
     */
    boolean existsByAlumnoAndEstado(Usuario alumno, EstadoPropuesta estado);


    /**
     * Calcula la suma total de horas de todas las propuestas APROBADAS de un alumno.
     * Utiliza una consulta JPQL personalizada para la agregación.
     *
     * @param alumno El usuario (alumno) cuyas horas se van a sumar.
     * @return Un {@link Optional<Long>} que contiene la suma.
     * Será {@link Optional#empty()} si el alumno no tiene propuestas aprobadas (el SUM es NULL).
     */
    // (Opcional, pero más eficiente para calcular horas)
    // Suma las horas de todas las propuestas APROBADAS de un alumno
    @Query("SELECT SUM(p.horasPropuestas) FROM Propuesta p WHERE p.alumno = :alumno AND p.estado = 'APROBADO'")
    Optional<Long> sumHorasAprobadasByAlumno(@Param("alumno") Usuario alumno);

}