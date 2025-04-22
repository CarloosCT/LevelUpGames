package org.agaray.pruebas.pap.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class Genero {

    //==========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    //==========================================================================

    public Genero(String nombre) 
    {
        this.nombre = nombre;
    }

    public Genero() 
    {
        this.nombre = "Género sin nombre";
    }

}
