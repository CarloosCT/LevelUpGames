package com.tfg.levelupgames.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.repositories.PrecioRepository;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;

    /**
     * Guarda un nuevo precio solo si no existe uno igual.
     * Si ya existe, retorna el existente.
     */
    public Precio save(BigDecimal  cantidad) {
        Optional<Precio> precioExistente = precioRepository.findByCantidad(cantidad);
        return precioExistente.orElseGet(() -> precioRepository.save(new Precio(cantidad)));
    }

    public List<Precio> findAll() {
        return precioRepository.findAll();
    }

    public Precio findByCantidad(BigDecimal  cantidad) {
        return precioRepository.findByCantidad(cantidad).orElse(null);
    }

    public Precio findById(Long precioId) {
        return precioRepository.findById(precioId).orElse(null);
    }

    public void d(Long id) throws Exception {
        Precio precioABorrar = precioRepository.findById(id).get();
        if (precioABorrar.getJuegos().isEmpty()) {
            precioRepository.deleteById(id);
        } else {
            throw new Exception("El precio " + precioABorrar.getCantidad() + " tiene juegos asociados");
        }
    }

    public Precio u(Long id, BigDecimal  cantidad) {
        Precio precioAModificar = precioRepository.findById(id).get();
        precioAModificar.setCantidad(cantidad);
        return precioRepository.save(precioAModificar);
    }
}
