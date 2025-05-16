package com.tfg.levelupgames.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Imagen;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.repositories.ImagenRepository;

import java.nio.file.Path;

@Service
public class ImagenService 
{
    @Autowired
    private ImagenRepository imagenRepository;

    public void save(Imagen imagen) 
    {
        imagenRepository.save(imagen);
    }

    public List<Imagen> findAll() {
        return imagenRepository.findAll();
    }

    public Imagen findByNombre(String ruta) {
        return imagenRepository.findByRuta(ruta).orElse(null);
    }

    public Imagen findById(Long id) {
        return imagenRepository.findById(id).get();
    }

    public void d(Long id) throws DangerException {
        Imagen imagen = this.findById(id);

        Path ruta = Paths.get("uploads/" + imagen.getRuta());
        try {
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            throw new DangerException("Error al eliminar archivo del disco: " + e.getMessage());
        }

        imagenRepository.deleteById(id);
    }

     public void u(Long id, String nuevaRuta) throws DangerException {
        Imagen imagen = this.findById(id);
        imagen.setRuta(nuevaRuta);
        imagenRepository.save(imagen);
    }

    public void procesarImagenesDeJuego(Juego juego, MultipartFile portada, MultipartFile[] imagenes) {
    try {
        Path uploadPath = Paths.get("uploads/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Guardar portada
        String nombrePortada = portada.getOriginalFilename();
        if (nombrePortada == null || nombrePortada.isEmpty()) {
            throw new RuntimeException("El nombre del archivo de portada es inválido.");
        }

        String nombreArchivoPortada = Paths.get(nombrePortada).getFileName().toString();
        Path rutaPortada = uploadPath.resolve(nombreArchivoPortada);

        Files.copy(portada.getInputStream(), rutaPortada, StandardCopyOption.REPLACE_EXISTING);

        Imagen portadaGuardada = new Imagen(nombreArchivoPortada, true);
        portadaGuardada.setJuego(juego);
        save(portadaGuardada);
        juego.setPortada(portadaGuardada);

        // Guardar imágenes adicionales
        List<Imagen> imagenesGuardadas = new ArrayList<>();
        for (MultipartFile imagen : imagenes) {
            if (imagen.isEmpty()) continue;

            String nombreArchivo = Paths.get(imagen.getOriginalFilename()).getFileName().toString();
            Path rutaAbsoluta = uploadPath.resolve(nombreArchivo);

            Files.copy(imagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);

            Imagen imagenGuardada = new Imagen(nombreArchivo);
            imagenGuardada.setJuego(juego);
            save(imagenGuardada);

            imagenesGuardadas.add(imagenGuardada);
        }

        juego.setImagenes(imagenesGuardadas);

    } catch (IOException e) {
        throw new RuntimeException("Error al guardar las imágenes: " + e.getMessage(), e);
    }
}
}
