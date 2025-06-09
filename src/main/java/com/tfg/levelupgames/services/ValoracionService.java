package com.tfg.levelupgames.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.entities.Valoracion;
import com.tfg.levelupgames.repositories.ValoracionRepository;

@Service
public class ValoracionService {
    
    @Autowired
    private ValoracionRepository valoracionRepository;

    public void save(Juego juego, Usuario usuario, BigDecimal valoracion) {
        Optional<Valoracion> optValoracion = valoracionRepository.findByUsuarioAndJuego(usuario, juego);

        if (optValoracion.isPresent()) {
            Valoracion v = optValoracion.get();
            v.setValor(valoracion);
            valoracionRepository.save(v);
        } else {
            Valoracion nuevaValoracion = new Valoracion(usuario, juego, valoracion);
            valoracionRepository.save(nuevaValoracion);
        }
    }

    public Float obtenerValoracionDeUsuario(Usuario usuario, Juego juego) {
        if (usuario == null) return null;

        Optional<Valoracion> optValoracion = valoracionRepository.findByUsuarioAndJuego(usuario, juego);

        if (optValoracion.isPresent()) {
            BigDecimal valor = optValoracion.get().getValor();
            return (valor != null) ? valor.floatValue() : null;
        }

        return null;
    }
}
