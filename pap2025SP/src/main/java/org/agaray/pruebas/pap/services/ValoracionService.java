package org.agaray.pruebas.pap.services;

import org.agaray.pruebas.pap.entities.Valoracion;
import org.agaray.pruebas.pap.repositories.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValoracionService {
     @Autowired
    private ValoracionRepository valoracionRepository;

    public void save(Valoracion valoracion) {
        valoracionRepository.save(valoracion);
    }
}