package com.tfg.levelupgames.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginemail;
    private String nombre;
    private String apellido;
    private String password;
    private boolean mostrarAlertaRechazo;
    private BigDecimal saldo;

    /*
     * @OneToMany(mappedBy = "usuario")
     * private List<Compra> compras;
     */

    @OneToMany(mappedBy = "usuario")
    private List<Valoracion> valoraciones;

    @ManyToOne
    @JoinColumn(name = "rol_nombre")
    private Rol rol;

    @OneToMany(mappedBy = "desarrollador")
    private List<Juego> juegosCreados = new ArrayList<>();

    public Usuario(String loginemail, String nombre, String apellido, String password) {
        this.loginemail = loginemail;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.saldo = BigDecimal.ZERO;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isAdmin() {
        return rol != null && "admin".equalsIgnoreCase(rol.getNombre());
    }

    public BigDecimal getSaldo() 
    {
        return this.saldo;
    }

    public void setSaldo(BigDecimal saldo) 
    {
        this.saldo = saldo;
    }
}
