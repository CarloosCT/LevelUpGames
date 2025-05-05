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

        List<Genero> generos = generoService.findByIds(generosIds);
        Precio precio = precioService.findById(precioId);
    
        Juego juego = new Juego(nombre, generos, precio, new ArrayList<>());
        juego = juegoRepository.save(juego);
    
        List<Imagen> imagenesGuardadas = new ArrayList<>();
    
        try {
            // Ruta relativa dentro del proyecto
            Path uploadPath = Paths.get("uploads/");
            
            // Crear carpeta si no existe
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
    
            for (MultipartFile imagen : imagenes) {
                String nombreArchivoOriginal = imagen.getOriginalFilename();
    
                if (nombreArchivoOriginal == null || nombreArchivoOriginal.isEmpty()) {
                    throw new RuntimeException("El nombre del archivo es inválido.");
                }
    
                // Limpiar el nombre del archivo
                String nombreArchivo = Paths.get(nombreArchivoOriginal).getFileName().toString();
                Path rutaAbsoluta = uploadPath.resolve(nombreArchivo);
    
                Files.copy(imagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);
    
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
}
