package com.tfg.levelupgames.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.services.CompraService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaController {

    @Autowired
    private CompraService compraService;

    @GetMapping("r")
    public String r(HttpSession session, ModelMap m) throws DangerException {
    Usuario usuario = (Usuario) session.getAttribute("user");

    if (usuario == null) {
        PRG.error("Debes iniciar sesión para ver tu biblioteca", "/usuario/login");
    }

    List<Juego> juegosComprados = compraService.findJuegosByUsuario(usuario);

    m.put("juegos", juegosComprados);
    m.put("view", "biblioteca/r");
    m.put("estilos", "/css/biblioteca/c.css");

    return "_t/frame";
}
}