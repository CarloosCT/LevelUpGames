package com.tfg.levelupgames.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column
    private String descargable;

    private String descargablePublicId;

    @Column(name = "nombre_descargable_original")
    private String nombreDescargableOriginal;

    @ManyToMany
    private List<Genero> generos = new ArrayList<>();

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portada_id")
    private Imagen portada;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Precio> precios = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "desarrollador_id", nullable = false)
    private Usuario desarrollador;

    @OneToMany(mappedBy = "juego")
    private List<Valoracion> valoraciones = new ArrayList<>();

    @OneToMany(mappedBy = "juego")
    private List<Compra> compras = new ArrayList<>();

    public Juego(String nombre, String descripcion, List<Genero> generos, List<Precio> precios, List<Imagen> imagenes,
            String descargable) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.generos = generos;
        this.precios = precios;
        this.imagenes = imagenes;
        this.descargable = descargable;
    }

    public void setPortada(Imagen portada) {
        this.portada = portada;
    }

    public String getNombresGeneros() {
        return generos.stream()
                .map(Genero::getNombre)
                .collect(Collectors.joining(","));
    }

    public Precio getPrecioActual() {
        if (precios == null || precios.isEmpty()) {
            return null;
        }
        return precios.stream()
                .filter(p -> p.getFechaFin() == null)
                .findFirst()
                .orElse(null);
    }

    public void setPrecio(Precio nuevoPrecio) {
        if (precios == null) {
            precios = new ArrayList<>();
        }

        Precio precioActual = getPrecioActual();
        if (precioActual != null && precioActual != nuevoPrecio) {
            precioActual.setFechaFin(LocalDate.now());
        }

        nuevoPrecio.setFechaFin(null);
        nuevoPrecio.setJuego(this);

        if (!precios.contains(nuevoPrecio)) {
            precios.add(nuevoPrecio);
        }
    }

    public void setDescargable(String descargable) {
        this.descargable = descargable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Juego))
            return false;
        Juego juego = (Juego) o;
        return id != null && id.equals(juego.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setDesarrollador(Usuario desarrollador) {
        this.desarrollador = desarrollador;
    }

    public List<Imagen> getImagenesSinPortada() {
        return imagenes.stream()
                .filter(imagen -> !imagen.isPortada())
                .collect(Collectors.toList());
    }

    public float getValoracionMedia() {
        if (valoraciones == null || valoraciones.isEmpty()) {
            return 0f;
        }

        BigDecimal suma = BigDecimal.ZERO;
        int contador = 0;

        for (Valoracion v : valoraciones) {
            if (v.getNota() != null) {
                suma = suma.add(v.getNota());
                contador++;
            }
        }

        if (contador == 0) {
            return 0f;
        }

        BigDecimal media = suma.divide(BigDecimal.valueOf(contador), 2, RoundingMode.HALF_UP);
        return media.floatValue();
    }
}
