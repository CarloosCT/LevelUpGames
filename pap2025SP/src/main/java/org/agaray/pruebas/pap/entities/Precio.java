package org.agaray.pruebas.pap.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
