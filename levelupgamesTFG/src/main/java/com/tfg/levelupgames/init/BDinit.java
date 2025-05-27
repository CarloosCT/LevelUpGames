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
    private GeneroService generoService;
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private ImagenService imagenService;
    /*@Autowired
    private ValoracionService valoracionService;*/
    @Autowired
    private PrecioService precioService;

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

        if (generoService.findAll().isEmpty()) {
            crearGenerosYDesarrolladores();
        }

        if (juegoService.findAll().isEmpty()) {
            crearJuegos();
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

    private void crearGenerosYDesarrolladores() {
        generoService.save("Aventura");
        generoService.save("Deportes");
    }

    private void crearJuegos() {
    Genero aventura = generoService.findByNombre("Aventura");
    java.math.BigDecimal cantidad = new java.math.BigDecimal("59.99");

    // --- Juego 1 ---
    Juego juego1 = new Juego();
    juego1.setNombre("Assassin's Creed");
    juego1.setDescripcion("Un juego de aventuras históricas.");
    juego1.setGeneros(List.of(aventura));
    juego1.setDescargable("Assassin's Creed.iso");  // <-- aquí la ruta o archivo
    juegoService.save(juego1);

    precioService.save(cantidad, juego1);

    Imagen portada1 = new Imagen("acreed1.jpg", true);
    portada1.setJuego(juego1);
    Imagen imgExtra1 = new Imagen("acreed2.jpg");
    imgExtra1.setJuego(juego1);
    imagenService.save(portada1);
    imagenService.save(imgExtra1);

    juego1.setPortada(portada1);
    juegoService.save(juego1);

    // --- Juego 2 ---
    Juego juego2 = new Juego();
    juego2.setNombre("Far Cry");
    juego2.setDescripcion("Acción en mundo abierto en escenarios exóticos.");
    juego2.setGeneros(List.of(aventura));
    juego2.setDescargable("Far Cry.rar");
    juegoService.save(juego2);

    precioService.save(cantidad, juego2);

    Imagen portada2 = new Imagen("farcry1.jpg", true);
    portada2.setJuego(juego2);
    Imagen imgExtra2 = new Imagen("farcry2.jpg");
    imgExtra2.setJuego(juego2);
    imagenService.save(portada2);
    imagenService.save(imgExtra2);

    juego2.setPortada(portada2);
    juegoService.save(juego2);

    // --- Juego 3 ---
    Juego juego3 = new Juego();
    juego3.setNombre("Watch Dogs");
    juego3.setDescripcion("Hackea el sistema en esta aventura urbana.");
    juego3.setGeneros(List.of(aventura));
    juego3.setDescargable("Watch Dogs.exe");
    juegoService.save(juego3);

    precioService.save(cantidad, juego3);

    Imagen portada3 = new Imagen("watchdogs1.jpg", true);
    portada3.setJuego(juego3);
    Imagen imgExtra3 = new Imagen("watchdogs2.jpg");
    imgExtra3.setJuego(juego3);
    imagenService.save(portada3);
    imagenService.save(imgExtra3);

    juego3.setPortada(portada3);
    juegoService.save(juego3);

    // --- Juego 4 ---
    Juego juego4 = new Juego();
    juego4.setNombre("Prince of Persia");
    juego4.setDescripcion("Aventura mítica con combates y acertijos.");
    juego4.setGeneros(List.of(aventura));
    juego4.setDescargable("Prince of Persia.setup");
    juegoService.save(juego4);

    precioService.save(cantidad, juego4);

    Imagen portada4 = new Imagen("pop1.jpg", true);
    portada4.setJuego(juego4);
    Imagen imgExtra4 = new Imagen("pop2.jpg");
    imgExtra4.setJuego(juego4);
    imagenService.save(portada4);
    imagenService.save(imgExtra4);

    juego4.setPortada(portada4);
    juegoService.save(juego4);
}
}