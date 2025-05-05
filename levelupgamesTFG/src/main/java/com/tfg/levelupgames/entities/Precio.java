package com.tfg.levelupgames.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Precio {

    //==========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Double cantidad;

    //Relaciones

    @OneToMany(mappedBy = "precio")
    private List<Juego> juegos = new ArrayList<>();

    //==========================================================================

    public Precio(Double cantidad) 
    {
        this.cantidad = cantidad;
    }

    public Precio() 
    {
        this.cantidad = 0.0;
    }
}
