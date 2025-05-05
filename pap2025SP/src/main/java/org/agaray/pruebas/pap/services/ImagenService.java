package org.agaray.pruebas.pap.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.agaray.pruebas.pap.entities.Imagen;
import org.agaray.pruebas.pap.exception.DangerException;
import org.agaray.pruebas.pap.repositories.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
