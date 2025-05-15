package com.tfg.levelupgames.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(exclude = "juegos")
@ToString(exclude = "juegos")
public class Precio {

    // ==========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal  cantidad;

    // Relaciones

    @OneToMany(mappedBy = "precio")
    private List<Juego> juegos = new ArrayList<>();

    // ==========================================================================

    public Precio(BigDecimal  cantidad) {
        this.cantidad = cantidad;
    }

    public Precio() {
    }
}
