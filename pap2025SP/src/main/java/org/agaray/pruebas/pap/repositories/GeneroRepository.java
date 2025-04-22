package org.agaray.pruebas.pap.repositories;

import org.agaray.pruebas.pap.entities.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero,Long> {
}
