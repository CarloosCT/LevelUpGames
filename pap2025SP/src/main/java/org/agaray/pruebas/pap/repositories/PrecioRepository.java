package org.agaray.pruebas.pap.repositories;

import java.util.Optional;

import org.agaray.pruebas.pap.entities.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {
    Optional<Precio> findByCantidad(Double cantidad);
    Optional<Precio> findById(int id);
}
