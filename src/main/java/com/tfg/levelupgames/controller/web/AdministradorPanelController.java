package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.ImagenService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.PrecioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/panel_administrador")
public class AdministradorPanelController {
    @Autowired
    private GeneroService generoService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private JuegoService juegoService;

    @GetMapping("r")
    public String r(HttpSession session, ModelMap m, @RequestParam(required = false) String success,
            @RequestParam(required = false) String error) {
        Usuario u = (Usuario) session.getAttribute("user");

        if (u == null || !u.isAdmin()) {
            return "redirect:/";
        }

        m.put("generos", generoService.findAll());
        m.put("precios", precioService.findAll());
        m.put("imagenes", imagenService.findAll());
        m.put("juegos", juegoService.findAll());
        m.put("view", "panel_administrador/r");
        m.put("estilos", "/css/panelAdministrador/panel.css");
        return "_t/frame";
    }
}