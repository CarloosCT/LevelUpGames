package com.tfg.levelupgames.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Imagen;
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

        Path ruta = Paths.get("src/main/resources/static/uploads/" + imagen.getRuta());
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
}
