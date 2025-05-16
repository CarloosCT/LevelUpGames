package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.JuegoService;

@Controller
public class HomeController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private GeneroService generoService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String success, @RequestParam(required = false) String cancel,
            ModelMap m) {
        m.put("view", "/home/home");
        m.put("juegos", juegoService.findAll());
        m.put("generos", generoService.findAll());
        m.put("estilos", "/css/home/home.css");

        if (success != null) {
            m.put("success", success);
        }
        if (cancel != null) {
            m.put("cancel", cancel);
        }

        return "_t/frame";
    }
}