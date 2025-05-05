package com.tfg.levelupgames.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByNombre(String nombre);
}
