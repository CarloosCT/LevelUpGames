package com.tfg.levelupgames.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    @Column(nullable = false)
    private Float nota;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    public Valoracion(String comentario, Float nota, Usuario usuario, Juego juego) {
        this.comentario = comentario;
        this.nota = nota;
        this.usuario = usuario;
        this.juego = juego;
    }
}
