package org.agaray.pruebas.pap.services;

import java.util.List;
import org.agaray.pruebas.pap.entities.Precio;
import org.agaray.pruebas.pap.repositories.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrecioService 
{
    @Autowired
    private PrecioRepository precioRepository;

    public void save(Double precio) {
        precioRepository.save( new Precio(precio));
    }

    public List<Precio> findAll() {
        return precioRepository.findAll();
    }

    public Precio findByNombre(Double precio) {
        return precioRepository.findByPrecio(precio).orElse(null);
    }
}
