package org.agaray.pruebas.pap.services;

import org.agaray.pruebas.pap.entities.Usuario;
import org.agaray.pruebas.pap.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
