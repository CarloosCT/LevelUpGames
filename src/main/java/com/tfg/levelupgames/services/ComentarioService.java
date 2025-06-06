package com.tfg.levelupgames.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tfg.levelupgames.entities.Comentario;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.ReporteComentario;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.repositories.ComentarioRepository;
import com.tfg.levelupgames.repositories.JuegoRepository;
import com.tfg.levelupgames.repositories.ReporteComentarioRepository;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ReporteComentarioRepository reporteComentarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    public List<Comentario> getComentariosPorJuegoId(Long juegoId) {
        return comentarioRepository.findByJuegoId(juegoId);
    }

    public void save(Long juegoId, Usuario usuario, String contenido) throws DangerException {

        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new DangerException("Juego no encontrado"));

        comentarioRepository.save(new Comentario(contenido, usuario, juego));

    }

    public Long getJuegoIdByComentarioId(Long comentarioId) {
        return comentarioRepository.findById(comentarioId)
                .map(c -> c.getJuego().getId())
                .orElse(null);
    }

    public void reportarComentario(Long comentarioId, Usuario usuario, String motivo) throws Exception {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new Exception("Comentario no encontrado"));

        Optional<ReporteComentario> yaReportado = reporteComentarioRepository
                .findByComentarioIdAndUsuarioId(comentarioId, usuario.getId());
        if (yaReportado.isPresent()) {
            throw new Exception("Ya has reportado este comentario.");
        }

        ReporteComentario reporte = new ReporteComentario();
        reporte.setComentario(comentario);
        reporte.setUsuario(usuario);
        reporte.setMotivo(motivo);
        reporte.setFecha(LocalDate.now());

        reporteComentarioRepository.save(reporte);
    }
}
