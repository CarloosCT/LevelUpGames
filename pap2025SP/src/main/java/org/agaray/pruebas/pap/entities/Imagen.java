package org.agaray.pruebas.pap.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagen;

    /*@ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;*/

    public Imagen(String imagen){
        this.imagen = imagen;
    }

    public Imagen(){
        this.imagen = "Juego sin imagen";
    }
}
