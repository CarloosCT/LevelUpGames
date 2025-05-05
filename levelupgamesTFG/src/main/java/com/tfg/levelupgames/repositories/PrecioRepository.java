package com.tfg.levelupgames.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Precio;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {
    Optional<Precio> findByCantidad(Double cantidad);
    Optional<Precio> findById(int id);
}
