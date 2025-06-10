package com.tfg.levelupgames.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
@Entity
@Data
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "generos")
    private List<Juego> juegos = new ArrayList<>();

    public Genero(String nombre) 
    {
        this.nombre = nombre;
    }

    public Genero() 
    {
        this.nombre = "Género sin nombre";
    }

}
