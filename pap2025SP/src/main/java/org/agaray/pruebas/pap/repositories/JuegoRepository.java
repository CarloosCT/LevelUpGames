package org.agaray.pruebas.pap.repositories;

import org.agaray.pruebas.pap.entities.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego,Long>{
}
