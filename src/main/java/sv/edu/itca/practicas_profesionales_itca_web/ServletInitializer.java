package sv.edu.itca.practicas_profesionales_itca_web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PracticasProfesionalesItcaWebApplication.class);
	}

}
//revisar logica en postman hice tres pruebas pero me dio error en post