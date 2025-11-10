package sv.edu.itca.practicas_profesionales_itca_web.dto;

import lombok.Data;
import sv.edu.itca.practicas_profesionales_itca_web.model.Area;
import sv.edu.itca.practicas_profesionales_itca_web.model.Rol;

@Data // Lombok crea getters/setters
public class CrearUsuarioRequest {
    // Los campos que el coordinador llenará en el formulario
    private String nombre;
    private String apellido;
    private String correoInstitucional;
    private Rol rol; //solo permitiremos 'ALUMNO'
    private Area area;
    // (La contraseña será temporal y la asignará el servicio)
}