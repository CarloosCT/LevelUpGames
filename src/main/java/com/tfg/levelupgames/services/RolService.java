package com.tfg.levelupgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Rol;
import com.tfg.levelupgames.repositories.RolRepository;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;

    public void save(String nombre)  {
        rolRepository.save( new Rol(nombre) );
    }

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Rol findByNombre(String nombre) {
        return rolRepository.findRolByNombre(nombre);
    }
}
