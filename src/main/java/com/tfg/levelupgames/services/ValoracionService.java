package com.tfg.levelupgames.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Valoracion;
import com.tfg.levelupgames.repositories.ValoracionRepository;

@Service
public class ValoracionService {
     @Autowired
    private ValoracionRepository valoracionRepository;

    public void save(Valoracion valoracion) {
        valoracionRepository.save(valoracion);
    }
}