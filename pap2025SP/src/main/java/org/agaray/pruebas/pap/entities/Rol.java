package org.agaray.pruebas.pap.entities;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "roles")
    private Collection<Persona> personas;


    //==========================================================================

    public Rol(String nombre) {
        this.nombre = nombre;
        this.personas = new ArrayList<>();
    }

    public Rol() {
        this.personas = new ArrayList<>();
    }
    //=============================
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

}
