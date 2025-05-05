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
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @GetMapping("/test")
    public void test(
    ) throws Exception {
        throw new DangerException("¡¡¡¡ PUM !!!!!");
    }

    @GetMapping("/contador")
    public String contador(
        HttpSession s,
        ModelMap m
    ) {
        if ( s.getAttribute("nVisitas") == null  )   {
            s.setAttribute("nVisitas", 1);
        }
        m.put("visitas",s.getAttribute("nVisitas"));
        
        s.setAttribute("nVisitas", ((Integer)s.getAttribute("nVisitas"))+1);
        return "home/contador";
    }
}
