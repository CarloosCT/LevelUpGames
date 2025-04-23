package org.agaray.pruebas.pap.services;

import org.agaray.pruebas.pap.entities.Juego;
import org.agaray.pruebas.pap.repositories.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuegoService {
    @Autowired
    private JuegoRepository juegoRepository;

    public void save(Juego juego) {
        juegoRepository.save(juego);
    }
}
