package com.tfg.levelupgames.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.repositories.PrecioRepository;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;

    public void save(BigDecimal cantidad, Juego juego) {
        Precio nuevoPrecio = new Precio(cantidad, juego);
        precioRepository.save(nuevoPrecio);
    }

    public List<Precio> findAll() {
        return precioRepository.findAll();
    }

    public Optional<Precio> findByCantidad(BigDecimal cantidad) {
        return precioRepository.findByCantidad(cantidad);
    }

    public Precio findById(Long precioId) {
        return precioRepository.findById(precioId).orElse(null);
    }

    public void delete(Long id) throws Exception {
        Precio precioABorrar = precioRepository.findById(id)
            .orElseThrow(() -> new Exception("Precio no encontrado"));
        if (precioABorrar.getJuego() == null) {
            precioRepository.deleteById(id);
        } else {
            throw new Exception("El precio " + precioABorrar.getCantidad() + " tiene un juego asociado");
        }
    }

    public Precio update(Long id, BigDecimal cantidad) throws Exception {
        Precio precioAModificar = precioRepository.findById(id)
            .orElseThrow(() -> new Exception("Precio no encontrado"));
        precioAModificar.setCantidad(cantidad);
        return precioRepository.save(precioAModificar);
    }

    public Precio findByJuegoCantidadFechaFinNull(Juego juego, BigDecimal cantidad) {
        List<Precio> precios = precioRepository.findByJuegoAndCantidadAndFechaFinIsNull(juego, cantidad);
        if (precios.isEmpty()) {
            return null;
        }
        return precios.get(0);
    }

    public Precio findMasCercanoAntesDeFecha(Juego juego, LocalDate fecha) {
        return precioRepository.findTopByJuegoAndFechaInicioLessThanEqualOrderByFechaInicioDesc(juego, fecha)
            .orElse(null);
    }
}
