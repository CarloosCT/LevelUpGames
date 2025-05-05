package com.tfg.levelupgames.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {
    @Id
    private String correo;

    private String pwd;
    private String nombre;
    private String apellidos;

    /*@OneToMany(mappedBy = "usuario")
    private List<Compra> compras;*/

    @OneToMany(mappedBy = "usuario")
    private List<Valoracion> valoraciones;

    
    @ManyToOne
    @JoinColumn(name = "rol_nombre")
    private Rol rol;

    public Rol getRol() {
        return rol;
    }
}
