package org.agaray.pruebas.pap.services;

import org.agaray.pruebas.pap.entities.Imagen;
import org.agaray.pruebas.pap.repositories.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenService {
    @Autowired
    private ImagenRepository imagenRepository;

    public void save(Imagen imagen) {
        imagenRepository.save(imagen);
    }
}
