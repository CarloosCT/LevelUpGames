package org.agaray.pruebas.pap.repositories;

import java.util.Optional;

import org.agaray.pruebas.pap.entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen,Long>{
    Optional<Imagen> findByRuta(String ruta);

}