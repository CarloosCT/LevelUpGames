package com.tfg.levelupgames.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.tfg.levelupgames.entities.Comentario;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.repositories.ComentarioRepository;
import com.tfg.levelupgames.repositories.JuegoRepository;

@Service
public class ComentarioService {
    
    @Autowired
    private ComentarioRepository comentarioRepository;

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
}
