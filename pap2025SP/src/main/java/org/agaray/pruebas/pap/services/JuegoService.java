package org.agaray.pruebas.pap.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.agaray.pruebas.pap.entities.Genero;
import org.agaray.pruebas.pap.entities.Imagen;
import org.agaray.pruebas.pap.entities.Juego;
import org.agaray.pruebas.pap.entities.Precio;
import org.agaray.pruebas.pap.repositories.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        // Obtener géneros seleccionados desde el servicio
        List<Genero> generos = generoService.findByIds(generosIds);

        // Obtener el precio seleccionado
        Precio precio = precioService.findById(precioId);

        // Crear el juego primero (sin las imágenes asociadas)
        Juego juego = new Juego(nombre, generos, precio, new ArrayList<>());

        // Guardar el juego para obtener el id
        juego = juegoRepository.save(juego);

        // Lista de imágenes asociadas al juego
        List<Imagen> imagenesGuardadas = new ArrayList<>();

        try {
            for (MultipartFile imagen : imagenes) {
                String nombreArchivo = imagen.getOriginalFilename();

                if (nombreArchivo == null || nombreArchivo.isEmpty()) {
                    throw new RuntimeException("El nombre del archivo es inválido.");
                }

                Path rutaAbsoluta = Paths.get("C:/Users/Usuario/Desktop/LevelUpGames/LevelUpGames/pap2025SP/src/main/resources/static/uploads/" + nombreArchivo);
                Files.createDirectories(rutaAbsoluta.getParent());
                Files.copy(imagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);

                // Crear imagen con la ruta y el juego
                Imagen imagenGuardada = new Imagen(nombreArchivo);
                imagenGuardada.setJuego(juego);

                // Guardar imagen con relación ya establecida
                imagenService.save(imagenGuardada);

                // Agregar a la lista
                imagenesGuardadas.add(imagenGuardada);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las imágenes: " + e.getMessage(), e);
        }

        // Asociar imágenes al juego
        juego.setImagenes(imagenesGuardadas);

        // Guardar nuevamente el juego con imágenes asociadas
        juegoRepository.save(juego);
    }

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }
}
