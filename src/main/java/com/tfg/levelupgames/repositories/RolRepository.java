package com.tfg.levelupgames.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.levelupgames.entities.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

   public Rol findRolByNombre(String nombre);
}
