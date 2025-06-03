package com.tfg.levelupgames.services;

import java.math.BigDecimal;

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
        valoracionRepository.save(new Valoracion(usuario, juego, valoracion));

    }
}