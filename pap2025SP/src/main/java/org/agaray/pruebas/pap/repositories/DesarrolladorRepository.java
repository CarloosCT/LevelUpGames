package org.agaray.pruebas.pap.repositories;

import java.util.Optional;

import org.agaray.pruebas.pap.entities.Desarrollador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesarrolladorRepository extends JpaRepository<Desarrollador, Long> {
    /*Optional<Desarrollador> findByNombre(String nombre);*/
}
