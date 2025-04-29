package org.agaray.pruebas.pap.entities;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @OneToMany(mappedBy = "rol")
    private Collection<Usuario> usuarios;

    public Rol(String nombre) {
        this.nombre = nombre;
        this.usuarios = new ArrayList<>();
    }

    public Rol() {
        this.usuarios = new ArrayList<>();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Rol rol = (Rol) obj;
        return nombre.equals(rol.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}