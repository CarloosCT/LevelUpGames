package com.tfg.levelupgames.entities;

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

    private String publicId;

    private boolean portada = false;

    @ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;

    public Imagen(String ruta, boolean portada) {
        this.ruta = ruta;
        this.portada = portada;
    }

    public Imagen(String ruta) {
        this.ruta = ruta;
    }

    public Imagen() {
        this.ruta = "Juego sin imagen";
    }

    public Imagen(String ruta, String publicId, boolean portada) {
        this.ruta = ruta;
        this.publicId = publicId;
        this.portada = portada;
    }

    public Imagen(String ruta, String publicId) {
        this.ruta = ruta;
        this.publicId = publicId;
        this.portada = false;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public boolean isPortada() {
        return portada;
    }

    public void setPortada(boolean portada) {
        this.portada = portada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Imagen))
            return false;
        Imagen imagen = (Imagen) o;
        return id != null && id.equals(imagen.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
