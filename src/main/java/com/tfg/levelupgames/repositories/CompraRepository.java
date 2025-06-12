package com.tfg.levelupgames.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Compra;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    boolean existsByUsuarioAndJuego(Usuario usuario, Juego juego);

    List<Compra> findByUsuario(Usuario usuario);

    List<Compra> findByUsuarioId(Long usuarioId);

    Page<Compra> findByUsuarioId(Long usuarioId, Pageable pageable);
}
