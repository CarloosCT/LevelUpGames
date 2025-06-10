package com.tfg.levelupgames.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Imagen;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.repositories.ImagenRepository;
import com.tfg.levelupgames.repositories.JuegoRepository;

@Service
public class ImagenService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private JuegoRepository juegoRepository;

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

    public void d(Long id) {
        Imagen imagen = imagenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        try {
            cloudinaryService.delete(imagen.getPublicId(), "image");
            imagenRepository.delete(imagen);
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando imagen en Cloudinary", e);
        }
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
            if (imagen.getPublicId() != null) {
                cloudinaryService.delete(imagen.getPublicId(), "image");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar imagen en Cloudinary: " + e.getMessage(), e);
        }
        imagenRepository.delete(imagen);
    }

    public void eliminarImagenesDeJuego(Juego juego) {
        List<Imagen> imagenes = new ArrayList<>(juego.getImagenes());
        for (Imagen imagen : imagenes) {
            if (!imagen.isPortada()) {
                eliminarImagenPorId(imagen.getId());
            }
        }
        juego.getImagenes().removeIf(imagen -> !imagen.isPortada());
    }

    public void procesarImagenesDeJuego(Juego juego, MultipartFile portadaFile, MultipartFile[] imagenes) {
        try {
            if (portadaFile != null && !portadaFile.isEmpty()) {
                Map<?, ?> uploadResult = cloudinaryService.upload(portadaFile);
                String url = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");

                Imagen portada = new Imagen();
                portada.setRuta(url);
                portada.setPublicId(publicId);
                portada.setPortada(true);
                portada.setJuego(juego);
                imagenRepository.save(portada);

                juego.setPortada(portada);
                juegoRepository.save(juego);
            }

            if (imagenes != null) {
                for (MultipartFile imagenFile : imagenes) {
                    if (imagenFile != null && !imagenFile.isEmpty()) {
                        Map<?, ?> uploadResult = cloudinaryService.upload(imagenFile);
                        String url = (String) uploadResult.get("secure_url");
                        String publicId = (String) uploadResult.get("public_id");

                        Imagen imagen = new Imagen();
                        imagen.setRuta(url);
                        imagen.setPublicId(publicId);
                        imagen.setPortada(false);
                        imagen.setJuego(juego);
                        imagenRepository.save(imagen);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imágenes a Cloudinary", e);
        }
    }
}