package com.tfg.levelupgames.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Juego;

@Repository
public interface JuegoRepository extends JpaRepository<Juego,Long>{
    List<Juego> findByGenerosNombre(String nombre);
}
