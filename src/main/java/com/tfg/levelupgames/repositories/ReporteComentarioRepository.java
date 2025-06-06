package com.tfg.levelupgames.repositories;

import com.tfg.levelupgames.entities.ReporteComentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReporteComentarioRepository extends JpaRepository<ReporteComentario, Long> {
    Optional<ReporteComentario> findByComentarioIdAndUsuarioId(Long comentarioId, Long usuarioId);

    List<ReporteComentario> findAllByOrderByFechaDesc();
}