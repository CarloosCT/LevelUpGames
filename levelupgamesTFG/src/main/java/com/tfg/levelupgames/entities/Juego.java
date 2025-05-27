package com.tfg.levelupgames.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "juego")
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "portada_id")
    private Imagen portada;

    @OneToMany(mappedBy = "juego")
    private List<Precio> precios = new ArrayList<>();

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

    public Juego(String nombre, String descripcion, List<Genero> generos, List<Precio> precios, List<Imagen> imagenes) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.generos = generos;
    this.precios = precios;
    this.imagenes = imagenes;
    }

    public void setPortada(Imagen portada) 
    {
        this.portada = portada;
    }

    public String getNombresGeneros() {
        return generos.stream()
                      .map(Genero::getNombre)
                      .collect(Collectors.joining(","));
    }

    public Precio getPrecioActual() {
    LocalDate hoy = LocalDate.now();
    return precios.stream()
        .filter(p -> !p.getFechaInicio().isAfter(hoy) &&
                     (p.getFechaFin() == null || !p.getFechaFin().isBefore(hoy)))
        .max(Comparator.comparing(Precio::getFechaInicio))
        .orElse(null);
}

public void setPrecio(Precio nuevoPrecio) {
    Precio precioActual = getPrecioActual(); // devuelve Precio o null
    if (precioActual != null && precioActual != nuevoPrecio) {
        precioActual.setFechaFin(LocalDate.now());
    }

    nuevoPrecio.setFechaFin(null);
    nuevoPrecio.setJuego(this);

    if (!precios.contains(nuevoPrecio)) {
        precios.add(nuevoPrecio);
    }
    }

}
