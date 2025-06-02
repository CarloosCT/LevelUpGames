package com.tfg.levelupgames.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import com.tfg.levelupgames.entities.Compra;
import com.tfg.levelupgames.entities.Precio;

public class CompraDTO {
    private Compra compra;
    private Precio precio;

    public CompraDTO(Compra compra, Precio precio) {
        this.compra = compra;
        this.precio = precio;
    }

    public Compra getCompra() {
        return compra;
    }

    public Precio getPrecio() {
        return precio;
    }

    public String getFechaCompraFormateada() {
        if (compra == null || compra.getFecha() == null) {
            return "Fecha no disponible";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return compra.getFecha().format(formatter);
    }

    public String getPrecioFormateado() {
        if (precio == null || precio.getCantidad() == null) {
            return "Sin precio";
        }
        BigDecimal cantidad = precio.getCantidad();
        if (cantidad.compareTo(BigDecimal.ZERO) == 0) {
            return "Gratis";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(cantidad) + " €";
    }
}
