package com.tfg.levelupgames.controller.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.dto.CompraDTO;
import com.tfg.levelupgames.entities.Compra;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.services.CompraService;
import com.tfg.levelupgames.services.PrecioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private PrecioService precioService;

    @GetMapping("r")
public String r(HttpSession session, ModelMap m) throws DangerException {
    Usuario usuario = (Usuario) session.getAttribute("user");

    if (usuario == null) {
        PRG.error("Debes iniciar sesión para ver tu biblioteca", "/usuario/login");
    }

    List<Compra> compras = compraService.findByUsuario(usuario);
    List<CompraDTO> comprasDTO = new ArrayList<>();

    for (Compra compra : compras) {
        Juego juego = compra.getJuego();
        LocalDate fechaCompra = compra.getFecha();

        // Buscar el precio más cercano anterior o igual a la fecha de compra
        Precio precioCercano = precioService.findMasCercanoAntesDeFecha(juego, fechaCompra);

        // Crear DTO
        CompraDTO dto = new CompraDTO(compra, precioCercano);
        comprasDTO.add(dto);
    }

    m.put("compras", comprasDTO);
    m.put("view", "biblioteca/r");
    m.put("estilos", "/css/biblioteca/c.css");

    return "_t/frame";
}

}