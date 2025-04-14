package org.agaray.pruebas.pap.services;

import java.util.List;

import org.agaray.pruebas.pap.entities.Rol;
import org.agaray.pruebas.pap.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;

    public void c(String nombre)  {
        rolRepository.save( new Rol(nombre) );
    }

    public List<Rol> r() {
        return rolRepository.findAll();
    }

    public Rol findRolByName(String nombre) {
        return rolRepository.findRolByNombre(nombre);
    }

}
