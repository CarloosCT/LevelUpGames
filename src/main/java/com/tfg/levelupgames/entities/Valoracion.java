package com.tfg.levelupgames.entities;

import java.math.BigDecimal;

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

    @Column(nullable = false)
    private BigDecimal nota;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    public Valoracion(Usuario usuario, Juego juego, BigDecimal valoracion) {
        this.nota = valoracion;
        this.usuario = usuario;
        this.juego = juego;
    }

    public void setValor(BigDecimal valoracion) {
        this.nota = valoracion;
    }

    public BigDecimal getValor() {
        return this.nota;
    }
}
