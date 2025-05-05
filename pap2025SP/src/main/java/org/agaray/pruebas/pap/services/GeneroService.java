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

    public Genero findById(Long id) {
        return generoRepository.findById(id).get();
    }

    public void d(Long id) throws Exception {
        Genero generoABorrar = generoRepository.findById(id).get();
        if (generoABorrar.getJuegos().size() == 0) {
            generoRepository.deleteById(id);
        }
        else {
            throw new Exception("El genero "+generoABorrar.getNombre()+" tiene juegos asociados");
        }
    }

    public Genero u(Long id, String nombre) {
        Genero GeneroAModificar =  generoRepository.findById(id).get();
        GeneroAModificar.setNombre(nombre);
        return generoRepository.save(GeneroAModificar);
     }
}
