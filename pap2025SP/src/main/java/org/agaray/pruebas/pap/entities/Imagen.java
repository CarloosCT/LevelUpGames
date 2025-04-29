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

    private String ruta;

    //Relaciones

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    public Imagen(String ruta) {
        this.ruta = ruta;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public Imagen(){
        this.ruta = "Juego sin imagen";
    }
}
