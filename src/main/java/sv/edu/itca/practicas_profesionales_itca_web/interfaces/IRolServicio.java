package sv.edu.itca.practicas_profesionales_itca_web.interfaces;

import sv.edu.itca.practicas_profesionales_itca_web.modelos.Rol;

import java.util.Optional;

public interface IRolServicio {
    public Optional<Rol> buscarPorNombre(String nombre);
}
