package com.tfg.levelupgames.services;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Genero;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.repositories.JuegoRepository;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private ImagenService imagenService;

    public void save(Juego juego) {
        juegoRepository.save(juego);
    }

    public void saveJuegoConRelaciones(String nombre, String descripcion, List<Long> generosIds, BigDecimal precio,
        MultipartFile portada, MultipartFile[] imagenes) {

    List<Genero> generos = generoService.findByIds(generosIds);

    Juego juego = new Juego(nombre, descripcion, generos, null, new ArrayList<>());
    juego = juegoRepository.save(juego);

    precioService.save(precio, juego);

    Precio precioActual = precioService.findByJuegoCantidadFechaFinNull(juego, precio);

    juego.setPrecio(precioActual);

    imagenService.procesarImagenesDeJuego(juego, portada, imagenes);

    juegoRepository.save(juego);
}

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Juego findById(Long id) {
        return juegoRepository.findById(id).orElse(null);
    }

    public void d(Long id) throws Exception {
        Juego juegoABorrar = juegoRepository.findById(id).get();
        try {
            juegoRepository.deleteById(id);
            juegoABorrar.getImagenes().forEach(imagen -> {
                try {
                    imagenService.d(imagen.getId());
                    String ruta = imagen.getRuta();
                    Path path = Paths.get("uploads/" + ruta);
                    path.toFile().delete();
                } catch (Exception e) {
                    throw new RuntimeException("Error al eliminar la imagen: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            throw new Exception("No se pudo eliminar el juego " + juegoABorrar.getNombre());
        }
    }

    public List<Juego> findByGeneroNombre(String nombre) {
        return juegoRepository.findByGenerosNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return juegoRepository.existsByNombre(nombre);
    }

    /*
     * public Juego u(Long id, String nombre) {
     * Juego JuegoAModificar = juegoRepository.findById(id).get();
     * JuegoAModificar.setNombre(nombre);
     * return juegoRepository.save(JuegoAModificar);
     * }
     */
}
