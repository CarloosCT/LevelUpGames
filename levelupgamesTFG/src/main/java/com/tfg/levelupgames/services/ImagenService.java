package com.tfg.levelupgames.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Imagen;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.repositories.ImagenRepository;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    public void save(Imagen imagen) {
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

    public void eliminarImagenPorId(Long imgId) {
        Imagen imagen = imagenRepository.findById(imgId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada con id " + imgId));
        try {
            Path rutaArchivo = Paths.get("uploads").resolve(imagen.getRuta());
            Files.deleteIfExists(rutaArchivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al borrar archivo de imagen: " + e.getMessage(), e);
        }
        imagenRepository.delete(imagen);
    }

    public void eliminarImagenesDeJuego(Juego juego) {
        List<Imagen> imagenes = new ArrayList<>(juego.getImagenes());
        for (Imagen imagen : imagenes) {
            if (!imagen.getEsPortada()) {
                eliminarImagenPorId(imagen.getId());
            }
        }
        juego.getImagenes().removeIf(imagen -> !imagen.getEsPortada());
    }

    private String generarNombreArchivoUnico(String originalFilename) {
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i);
        }
        return UUID.randomUUID().toString() + extension;
    }

    public void procesarImagenesDeJuego(Juego juego, MultipartFile nuevaPortada, MultipartFile[] nuevasImagenes) {
    try {
        Path uploadPath = Paths.get("uploads/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 🔹 Eliminar portada anterior (si hay nueva)
        if (nuevaPortada != null && !nuevaPortada.isEmpty()) {
            if (juego.getPortada() != null) {
                eliminarImagenPorId(juego.getPortada().getId());
                juego.setPortada(null);
            }

            String nombreArchivoUnico = generarNombreArchivoUnico(nuevaPortada.getOriginalFilename());
            Path rutaPortada = uploadPath.resolve(nombreArchivoUnico);
            Files.copy(nuevaPortada.getInputStream(), rutaPortada, StandardCopyOption.REPLACE_EXISTING);

            Imagen nuevaImgPortada = new Imagen(nombreArchivoUnico, true);
            nuevaImgPortada.setJuego(juego);
            imagenRepository.save(nuevaImgPortada); // ✅ Guardamos en BD
            juego.setPortada(nuevaImgPortada); // ✅ Enlazamos al juego
        }

        // 🔹 Eliminar imágenes anteriores si hay nuevas
        boolean hayNuevasImagenes = nuevasImagenes != null && Arrays.stream(nuevasImagenes).anyMatch(img -> !img.isEmpty());

        if (hayNuevasImagenes) {
            List<Imagen> imagenesActuales = new ArrayList<>(juego.getImagenes());
            for (Imagen img : imagenesActuales) {
                if (!img.getEsPortada()) {
                    eliminarImagenPorId(img.getId());
                }
            }
            juego.getImagenes().removeIf(img -> !img.getEsPortada());

            for (MultipartFile imagen : nuevasImagenes) {
                if (imagen.isEmpty()) continue;

                String nombreArchivoUnico = generarNombreArchivoUnico(imagen.getOriginalFilename());
                Path rutaImagen = uploadPath.resolve(nombreArchivoUnico);
                Files.copy(imagen.getInputStream(), rutaImagen, StandardCopyOption.REPLACE_EXISTING);

                Imagen nuevaImagen = new Imagen(nombreArchivoUnico, false);
                nuevaImagen.setJuego(juego);
                juego.getImagenes().add(nuevaImagen);
            }
        }

    } catch (IOException e) {
        throw new RuntimeException("Error al procesar las imágenes: " + e.getMessage(), e);
    }
}
}
