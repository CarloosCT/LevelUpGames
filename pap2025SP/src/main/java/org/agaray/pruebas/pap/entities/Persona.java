package org.agaray.pruebas.pap.entities;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Persona {

    //==========================================================================
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String loginname;
    @JsonIgnore
    private String password;
    private String nombre;
    private String apellido;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Rol> roles;

    @ManyToOne
    private Pais nace;

    @ManyToOne
    private Pais vive;

    @ManyToMany
    private Collection<Aficion> gustos;

    @ManyToMany
    private Collection<Aficion> odios;

    //==========================================================================

    public Persona(String nombre, String apellido, String loginname, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.loginname = loginname;
        this.password = password;
        this.gustos = new ArrayList<>();
        this.odios = new ArrayList<>();
        this.roles = new ArrayList<>();
    }
    public Persona() {
        this.nombre = "John";
        this.apellido = "Doe";
        this.gustos = new ArrayList<>();
        this.odios = new ArrayList<>();
        this.roles = new ArrayList<>();
    }
    
}
