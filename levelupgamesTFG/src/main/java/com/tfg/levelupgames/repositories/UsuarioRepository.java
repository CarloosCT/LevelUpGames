package com.tfg.levelupgames.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,String>{
    Optional<Usuario> findByCorreo(String correo);
}