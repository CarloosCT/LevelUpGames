package com.tfg.levelupgames.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Desarrollador;

@Repository
public interface DesarrolladorRepository extends JpaRepository<Desarrollador, Long> {
    /*Optional<Desarrollador> findByNombre(String nombre);*/
}
