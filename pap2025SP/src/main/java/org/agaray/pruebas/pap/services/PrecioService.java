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

    public void save(Double cantidad) {
        precioRepository.save( new Precio(cantidad));
    }

    public List<Precio> findAll() {
        return precioRepository.findAll();
    }

    public Precio findByCantidad(Double cantidad) {
        return precioRepository.findByCantidad(cantidad).orElse(null);
    }

    public Precio findById(Long precioId) 
    {
        return precioRepository.findById(precioId).orElse(null);
    }
}
