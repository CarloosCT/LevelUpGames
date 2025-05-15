package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.services.JuegoService;

import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping("/")
    public String home(
        ModelMap m
    ) {
        m.put("view","/home/home");
        m.put("juegos", juegoService.findAll());
        m.put("estilos", "/css/home/home.css");
        return "_t/frame";
    }
}
