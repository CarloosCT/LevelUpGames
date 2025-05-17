package com.tfg.levelupgames.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg.levelupgames.entities.Rol;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.repositories.RolRepository;
import com.tfg.levelupgames.repositories.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    public void save(String loginemail, String nombre, String apellido, String password, String rolNombre) {
        Usuario u = new Usuario(loginemail, nombre, apellido, new BCryptPasswordEncoder().encode(password));

        Rol rol = rolRepository.findRolByNombre(rolNombre);
        u.setRol(rol);

        usuarioRepository.save(u);
    }

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Usuario findByCorreo(String email) {
        return usuarioRepository.findByLoginemail(email);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).get();
    }

    public Usuario login(String loginemail, String password) throws Exception {
        Usuario usuario = usuarioRepository.findByLoginemail(loginemail);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }
        if (!new BCryptPasswordEncoder().matches(password, usuario.getPassword())) {
            throw new Exception("Contraseña incorrecta");
        }
        return usuario;
    }

    public void updateBean(Usuario admin) {
        usuarioRepository.save(admin);
    }

    public Usuario obtenerUsuarioActual(HttpSession session) {
        return (Usuario) session.getAttribute("user");
    }

    public void sumarSaldo(Long usuarioId, BigDecimal cantidad) {
        Usuario usuario = usuarioRepository.findById(usuarioId).get();

        if (usuario.getSaldo() == null) {
            usuario.setSaldo(BigDecimal.ZERO);
        }
        usuario.setSaldo(usuario.getSaldo().add(cantidad));
        usuarioRepository.save(usuario);
    }
}