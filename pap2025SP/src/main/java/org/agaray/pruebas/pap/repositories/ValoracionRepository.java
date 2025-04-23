package org.agaray.pruebas.pap.repositories;

import org.agaray.pruebas.pap.entities.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion,Long>{
}