package com.tfg.levelupgames.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Compra;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.repositories.CompraRepository;

@Service
public class CompraService {
    
    @Autowired
    private CompraRepository compraRepository;

    public void save(Juego juego, Usuario usuario) {
        compraRepository.save(new Compra(juego, usuario));
    }

    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    public boolean existeCompra(Usuario usuario, Juego juego) {
    return compraRepository.existsByUsuarioAndJuego(usuario, juego);
    }

    public List<Compra> findByUsuario(Usuario usuario) {
    return compraRepository.findByUsuario(usuario);
    }
}