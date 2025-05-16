package com.tfg.levelupgames.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "juego_id")
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Compra(Juego juego, Usuario usuario) {
        this.juego = juego;
        this.usuario = usuario;
        this.fecha = LocalDate.now();
    }
}