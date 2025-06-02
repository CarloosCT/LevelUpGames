package com.tfg.levelupgames.controller.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.CompraService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.ValoracionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/valoracion")
public class ValoracionController {

    @Autowired
    private ValoracionService valoracionService;

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private CompraService compraService;

    @PostMapping("cpost")
    public String cPost(
    @RequestParam("juegoId") Long juegoId,
    @RequestParam("valoracion") BigDecimal valoracion,
    HttpSession session, ModelMap m) throws DangerException {

    Usuario usuario = (Usuario) session.getAttribute("user");

    if (usuario == null) {
        PRG.error("Debes iniciar sesión para valorar", "/usuario/login");
    }

    Juego juego = juegoService.findById(juegoId);
    if (juego == null) {
        PRG.error("Juego no encontrado", "/");
    }

if (!compraService.existeCompra(usuario, juego)) {
    PRG.error("Debes comprar un juego para valorarlo", "/juego/r/" + juegoId);
}

    try {
        valoracionService.save(juego, usuario, valoracion);
    } catch (Exception e) {
    e.printStackTrace();
    PRG.error("No se pudo guardar la valoración: " + e.getMessage(), "/juego/r/" + juegoId);
}

    return "redirect:/juego/r/" + juegoId;
}

}