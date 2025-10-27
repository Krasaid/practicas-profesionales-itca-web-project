package sv.edu.itca.practicas_profesionales_itca_web.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sv.edu.itca.practicas_profesionales_itca_web.modelos.Rol;

@Repository
public interface IRol extends CrudRepository<Rol, Double> {

}
