package com.tfg.levelupgames.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.entities.Valoracion;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion,Long>{
    Optional<Valoracion> findByUsuarioAndJuego(Usuario usuario, Juego juego);
}