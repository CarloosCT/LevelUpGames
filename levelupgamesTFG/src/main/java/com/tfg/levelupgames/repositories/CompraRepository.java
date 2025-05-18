package com.tfg.levelupgames.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Compra;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    /*Optional<Desarrollador> findByNombre(String nombre);*/
    boolean existsByUsuarioAndJuego(Usuario usuario, Juego juego);
}
