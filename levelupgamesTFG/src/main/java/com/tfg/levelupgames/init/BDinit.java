package com.tfg.levelupgames.init;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.tfg.levelupgames.entities.*;
import com.tfg.levelupgames.repositories.UsuarioRepository;
import com.tfg.levelupgames.services.*;

import java.util.List;

@Component
public class BDinit {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private RolService rolService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private GeneroService generoService;
    /*
     * @Autowired
     * private DesarrolladorService desarrolladorService;
     */
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private ImagenService imagenService;
    @Autowired
    private ValoracionService valoracionService;

    BDinit(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostConstruct
    public void init() {
        if (rolService.findAll().isEmpty()) {
            crearRoles();
        }

        String adminEmail = "admin1@gmail.com";
        if (usuarioRepository.findByLoginemail(adminEmail) == null) {
            crearUsuarioAdmin();
        }
    }

    private void crearRoles() {
        rolService.save("admin");
        rolService.save("user");
        rolService.save("developer");
    }

    private void crearUsuarioAdmin() {
        Rol adminRol = rolService.findByNombre("admin");
        Usuario admin = new Usuario("admin1@gmail.com", "Admin", "Admin", new BCryptPasswordEncoder().encode("1234"));
        admin.setRol(adminRol);
        usuarioRepository.save(admin);
    }

    /*
     * private void crearGenerosYDesarrolladores() {
     * generoService.save(new Genero("Aventura"));
     * generoService.save(new Genero("Deportes"));
     * 
     * desarrolladorService.save(new Desarrollador("Ubisoft"));
     * desarrolladorService.save(new Desarrollador("EA Sports"));
     * }
     */

    /*
     * private void crearJuegos() {
     * Genero genero = generoService.findByNombre("Aventura");
     * Desarrollador desarrollador = desarrolladorService.findByNombre("Ubisoft");
     * 
     * Juego juego = new Juego();
     * juego.setNombre("Assassin's Creed");
     * juego.setDescripcion("Un juego de aventuras históricas.");
     * juego.setGenero(genero);
     * juego.setDesarrollador(desarrollador);
     * 
     * juegoService.save(juego);
     * 
     * // Imágenes
     * imagenService.save(new Imagen("acreed1.jpg", juego));
     * imagenService.save(new Imagen("acreed2.jpg", juego));
     * 
     * // Valoraciones
     * Usuario usuario = usuarioService.findByCorreo("user@example.com");
     * valoracionService.save(new Valoracion("Muy bueno", 4.5f, usuario, juego));
     * valoracionService.save(new Valoracion("Me encantó", 5f, usuario, juego));
     * }
     */
}