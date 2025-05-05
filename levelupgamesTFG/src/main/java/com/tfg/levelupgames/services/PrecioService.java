package com.tfg.levelupgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.repositories.PrecioRepository;

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

     public void d(Long id) throws Exception {
        Precio PrecioABorrar = precioRepository.findById(id).get();
        if (PrecioABorrar.getJuegos().size() == 0) {
            precioRepository.deleteById(id);
        }
        else {
            throw new Exception("El precio "+PrecioABorrar.getCantidad()+" tiene juegos asociados");
        }
    }

    public Precio u(Long id, Double cantidad) {
        Precio PrecioAModificar =  precioRepository.findById(id).get();
        PrecioAModificar.setCantidad(cantidad);
        return precioRepository.save(PrecioAModificar);
     }
}
