package com.tfg.levelupgames.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    /*// No se guarda en la base de datos, se calcula a partir de las valoraciones
    @Transient
    private Float valoracionMedia;*/

    //Relaciones

    @ManyToMany
    private List<Genero> generos = new ArrayList<>();

    @ManyToOne
    private Precio precio;

    @OneToMany(mappedBy = "juego")
    private List<Imagen> imagenes = new ArrayList<>();

    /*@OneToMany(mappedBy = "juego")
    private List<Valoracion> valoraciones = new ArrayList<>();*/

    /*@OneToMany(mappedBy = "juego")
    private List<Compra> compras = new ArrayList<>();*/

    /*@ManyToOne
    @JoinColumn(name = "desarrollador_id", nullable = false)
    private Desarrollador desarrollador;*/

    /*// MÉTODO PARA CALCULAR LA MEDIA
    public Float getValoracionMedia() {
        if (valoraciones == null || valoraciones.isEmpty()) return 0f;

        float suma = 0f;
        for (Valoracion v : valoraciones) {
            suma += v.getNota();
        }
        return suma / valoraciones.size();
    }*/

    public Juego(String nombre, List<Genero> generos, Precio precio, List<Imagen> imagenes) {
        this.nombre = nombre;
        this.generos = generos;
        this.precio = precio;
        this.imagenes = imagenes;
    }
}
