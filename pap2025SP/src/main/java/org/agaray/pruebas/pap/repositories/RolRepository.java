package org.agaray.pruebas.pap.repositories;

import org.agaray.pruebas.pap.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

   public Rol findRolByNombre(String nombre);
}
