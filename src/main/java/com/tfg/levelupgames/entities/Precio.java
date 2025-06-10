package com.tfg.levelupgames.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    public Precio(BigDecimal cantidad) {
        this.cantidad = cantidad;
        this.fechaInicio = LocalDate.now();
    }

    public Precio(BigDecimal cantidad, LocalDate fechaInicio, LocalDate fechaFin) {
        this.cantidad = cantidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Precio(BigDecimal cantidad, Juego juego) {
        this.cantidad = cantidad;
        this.juego = juego;
        this.fechaInicio = LocalDate.now();
        this.fechaFin = null;
    }

    public Precio() {
    }
}
