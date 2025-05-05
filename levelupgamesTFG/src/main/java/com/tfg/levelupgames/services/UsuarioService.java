package com.tfg.levelupgames.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }
}
