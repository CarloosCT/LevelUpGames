package com.tfg.levelupgames.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen,Long>{
    Optional<Imagen> findByRuta(String ruta);

}