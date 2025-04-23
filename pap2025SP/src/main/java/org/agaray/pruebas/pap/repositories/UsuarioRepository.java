package org.agaray.pruebas.pap.repositories;

import java.util.Optional;

import org.agaray.pruebas.pap.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,String>{
    Optional<Usuario> findByCorreo(String correo);
}