package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.ImagenService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.PrecioService;

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
    public String r(ModelMap m) {
        m.put("generos", generoService.findAll());
        m.put("precios", precioService.findAll());
        m.put("imagenes", imagenService.findAll());
        m.put("juegos", juegoService.findAll());
        m.put("view", "panel_administrador/r");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }
}