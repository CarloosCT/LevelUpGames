package org.agaray.pruebas.pap.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruta;

    //Relaciones

    @ManyToOne
    private Juego juego;

    public Imagen(String ruta){
        this.ruta = ruta;
    }

    public Imagen(){
        this.ruta = "Juego sin imagen";
    }
}
