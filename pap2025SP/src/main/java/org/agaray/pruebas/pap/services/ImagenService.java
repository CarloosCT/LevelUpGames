package org.agaray.pruebas.pap.services;

import java.util.List;
import org.agaray.pruebas.pap.entities.Imagen;
import org.agaray.pruebas.pap.repositories.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenService 
{
    @Autowired
    private ImagenRepository imagenRepository;

    public void save(String ruta) 
    {
        imagenRepository.save( new Imagen(ruta));
    }

    public List<Imagen> findAll() {
        return imagenRepository.findAll();
    }

    public Imagen findByNombre(String ruta) {
        return imagenRepository.findByRuta(ruta).orElse(null);
    }
}
