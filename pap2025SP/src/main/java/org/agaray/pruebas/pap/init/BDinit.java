package org.agaray.pruebas.pap.init;

import jakarta.annotation.PostConstruct;
import org.agaray.pruebas.pap.entities.*;
import org.agaray.pruebas.pap.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/*@Component
public class BDinit {

    @Autowired private RolService rolService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private GeneroService generoService;
    @Autowired private DesarrolladorService desarrolladorService;
    @Autowired private JuegoService juegoService;
    @Autowired private ImagenService imagenService;
    @Autowired private ValoracionService valoracionService;

    @PostConstruct
    public void init() {
        if (rolService.findAll().isEmpty()) {
            crearRoles();
            crearUsuarios();
            crearGenerosYDesarrolladores();
            crearJuegos();
        }
    }

    private void crearRoles() {
        rolService.save("admin");
        rolService.save("user");
    }

    private void crearUsuarios() {
        Rol userRol = rolService.findByNombre("user");
        usuarioService.save(new Usuario("user@example.com", "1234", "Pepe", "Gómez", userRol));
    }

    private void crearGenerosYDesarrolladores() {
        generoService.save(new Genero("Aventura"));
        generoService.save(new Genero("Deportes"));

        desarrolladorService.save(new Desarrollador("Ubisoft"));
        desarrolladorService.save(new Desarrollador("EA Sports"));
    }

    private void crearJuegos() {
        Genero genero = generoService.findByNombre("Aventura");
        Desarrollador desarrollador = desarrolladorService.findByNombre("Ubisoft");

        Juego juego = new Juego();
        juego.setNombre("Assassin's Creed");
        juego.setDescripcion("Un juego de aventuras históricas.");
        juego.setGenero(genero);
        juego.setDesarrollador(desarrollador);

        juegoService.save(juego);

        // Imágenes
        imagenService.save(new Imagen("acreed1.jpg", juego));
        imagenService.save(new Imagen("acreed2.jpg", juego));

        // Valoraciones
        Usuario usuario = usuarioService.findByCorreo("user@example.com");
        valoracionService.save(new Valoracion("Muy bueno", 4.5f, usuario, juego));
        valoracionService.save(new Valoracion("Me encantó", 5f, usuario, juego));
    }
}*/
