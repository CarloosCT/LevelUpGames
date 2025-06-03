package com.tfg.levelupgames.controller.web;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.services.JuegoService;

@Controller
@RequestMapping("/panel_desarrollador")
public class DesarrolladorPanelController {

    @Autowired
    private JuegoService juegoservice;

    @GetMapping("r")
    public String r(ModelMap m, HttpSession session) throws DangerException {

        Usuario desarrollador = (Usuario) session.getAttribute("user");

        if (desarrollador == null) {
            m.put("view", "errores/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        List<Juego> juegos = juegoservice.findByDesarrollador(desarrollador);

        m.put("juegos", juegos);
        m.put("view", "panel_desarrollador/r");
        m.put("estilos", "/css/panelDesarrollador/r.css");
        return "_t/frame";
    }
}
