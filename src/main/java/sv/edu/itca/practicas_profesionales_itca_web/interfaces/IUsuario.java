package sv.edu.itca.practicas_profesionales_itca_web.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sv.edu.itca.practicas_profesionales_itca_web.modelos.Usuario;

@Repository
public interface IUsuario extends CrudRepository<Usuario, Double> {

}
