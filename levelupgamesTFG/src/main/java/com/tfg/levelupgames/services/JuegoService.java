package com.tfg.levelupgames.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Genero;
import com.tfg.levelupgames.entities.Imagen;
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

    public void saveJuegoConRelaciones(String nombre, List<Long> generosIds, Long precioId, MultipartFile[] imagenes) {

        List<Genero> generos = generoService.findByIds(generosIds);
        Precio precio = precioService.findById(precioId);

        Juego juego = new Juego(nombre, generos, precio, new ArrayList<>());
        juego = juegoRepository.save(juego);

        List<Imagen> imagenesGuardadas = new ArrayList<>();

        try {
            Path uploadPath = Paths.get("uploads/");

            // Crear la carpeta uploads si no existe
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile imagen : imagenes) {
                String nombreArchivoOriginal = imagen.getOriginalFilename();

                if (nombreArchivoOriginal == null || nombreArchivoOriginal.isEmpty()) {
                    throw new RuntimeException("El nombre del archivo es inválido.");
                }

                // Limpiar el nombre del archivo para evitar problemas con rutas
                String nombreArchivo = Paths.get(nombreArchivoOriginal).getFileName().toString();
                Path rutaAbsoluta = uploadPath.resolve(nombreArchivo);

                // Guardar la imagen en la carpeta uploads
                Files.copy(imagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);

                // Guardar la imagen
                Imagen imagenGuardada = new Imagen(nombreArchivo);
                imagenGuardada.setJuego(juego);
                imagenService.save(imagenGuardada);

                imagenesGuardadas.add(imagenGuardada);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las imágenes: " + e.getMessage(), e);
        }

        juego.setImagenes(imagenesGuardadas);
        juegoRepository.save(juego);
    }

    public List<Juego> findAll() {
        return juegoRepository.findAll();
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

    /*
     * public Juego u(Long id, String nombre) {
     * Juego JuegoAModificar = juegoRepository.findById(id).get();
     * JuegoAModificar.setNombre(nombre);
     * return juegoRepository.save(JuegoAModificar);
     * }
     */
}
