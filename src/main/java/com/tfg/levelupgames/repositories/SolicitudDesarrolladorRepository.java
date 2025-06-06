package com.tfg.levelupgames.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.levelupgames.entities.SolicitudDesarrollador;
import com.tfg.levelupgames.entities.Usuario;

public interface SolicitudDesarrolladorRepository extends JpaRepository<SolicitudDesarrollador, Long> {
    boolean existsByUsuario(Usuario usuario);

    List<SolicitudDesarrollador> findByRevisadaFalse();

    List<SolicitudDesarrollador> findByUsuarioAndRevisadaFalse(Usuario usuario);

    Optional<SolicitudDesarrollador> findByUsuario(Usuario usuario);

    Optional<SolicitudDesarrollador> findByUsuarioAndAprobadaTrue(Usuario usuario);

}