package org.agaray.pruebas.pap.entities;

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
    private Double precio;

    //Relaciones

    @OneToMany(mappedBy = "precio")
    private List<Juego> juegos = new ArrayList<>();

    //==========================================================================

    public Precio(Double precio) 
    {
        this.precio = precio;
    }

    public Precio() 
    {
        this.precio = 0.0;
    }
}
