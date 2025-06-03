package com.tfg.levelupgames.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface JuegoRepository extends JpaRepository<Juego,Long>{
    List<Juego> findByGenerosNombre(String nombre);
    boolean existsByNombre(String nombre);
    Page<Juego> findByDesarrollador(Usuario desarrollador, Pageable pageable);
    List<Juego> findByNombre(String nombre);
}
