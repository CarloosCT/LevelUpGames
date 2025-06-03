package com.tfg.levelupgames.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Precio;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {
    Optional<Precio> findByCantidad(BigDecimal cantidad);
    Optional<Precio> findById(int id);
    List<Precio> findByJuegoAndCantidadAndFechaFinIsNull(Juego juego, BigDecimal cantidad);
    Optional<Precio> findTopByJuegoAndFechaInicioLessThanEqualOrderByFechaInicioDesc(Juego juego, LocalDate fecha);
}
