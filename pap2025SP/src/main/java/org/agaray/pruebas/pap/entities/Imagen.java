package org.agaray.pruebas.pap.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruta;

    /*@ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;*/

    public Imagen(String ruta){
        this.ruta = ruta;
    }

    public Imagen(){
        this.ruta = "Juego sin imagen";
    }
}
