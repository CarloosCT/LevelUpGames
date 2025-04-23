package org.agaray.pruebas.pap.entities;
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

    @Column(length = 1000)
    private String descripcion;

    // No se guarda en la base de datos, se calcula a partir de las valoraciones
    @Transient
    private Float valoracionMedia;

    // RELACIONES

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Valoracion> valoraciones = new ArrayList<>();

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Precio> precios = new ArrayList<>();

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "genero_id", nullable = false)
    private Genero genero;

    @ManyToOne
    @JoinColumn(name = "desarrollador_id", nullable = false)
    private Desarrollador desarrollador;

    // MÉTODO PARA CALCULAR LA MEDIA
    public Float getValoracionMedia() {
        if (valoraciones == null || valoraciones.isEmpty()) return 0f;

        float suma = 0f;
        for (Valoracion v : valoraciones) {
            suma += v.getNota();
        }
        return suma / valoraciones.size();
    }
}
