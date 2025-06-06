package com.tfg.levelupgames.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Rol;
import com.tfg.levelupgames.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Usuario findByLoginemail(String loginemail);

    List<Usuario> findByRol(Rol rol);
}