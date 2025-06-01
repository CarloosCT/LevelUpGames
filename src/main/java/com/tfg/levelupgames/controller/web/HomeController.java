package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String success, 
                       @RequestParam(required = false) String cancel,
                       HttpSession session,
                       ModelMap m) {

        m.put("juegos", juegoService.findAll());

        m.put("generos", generoService.findAll());
        m.put("estilos", "/css/home/home.css");
        m.put("view", "home/home");

        if (success != null) {
            m.put("success", success);
        }
        if (cancel != null) {
            m.put("cancel", cancel);
        }

        Usuario usuario = usuarioService.obtenerUsuarioActual(session);
        if (usuario != null && usuario.isMostrarAlertaRechazo()) {
            m.put("alertaRechazo", "Tu solicitud como desarrollador ha sido rechazada.");
            usuario.setMostrarAlertaRechazo(false);
            usuarioService.save(usuario);
        }

        return "_t/frame";
    }
}