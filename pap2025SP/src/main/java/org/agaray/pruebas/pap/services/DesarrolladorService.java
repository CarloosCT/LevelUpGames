package org.agaray.pruebas.pap.services;

import org.agaray.pruebas.pap.entities.Desarrollador;
import org.agaray.pruebas.pap.repositories.DesarrolladorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesarrolladorService {
    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    public void save(Desarrollador desarrollador) {
        desarrolladorRepository.save(desarrollador);
    }

    public Desarrollador findByNombre(String nombre) {
        return desarrolladorRepository.findByNombre(nombre).orElse(null);
    }
}
