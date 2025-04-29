package org.agaray.pruebas.pap.services;

import java.util.List;
import org.agaray.pruebas.pap.entities.Genero;
import org.agaray.pruebas.pap.repositories.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneroService 
{
    @Autowired
    private GeneroRepository generoRepository;

    public void save(String nombre) {
        generoRepository.save(new Genero(nombre));
    }

    public List<Genero> findAll() {
        return generoRepository.findAll();
    }

    public Genero findByNombre(String nombre) {
        return generoRepository.findByNombre(nombre).orElse(null);
    }

    public List<Genero> findByIds(List<Long> ids) {
        return generoRepository.findAllById(ids);
    }
}
