package com.tfg.levelupgames.controller.web;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public String r(ModelMap m, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) throws DangerException {

        Usuario desarrollador = (Usuario) session.getAttribute("user");

        if (desarrollador == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Juego> juegosPage = juegoservice.findByDesarrollador(desarrollador, pageable);

        m.put("juegos", juegosPage.getContent());
        m.put("currentPage", page);
        m.put("totalPages", juegosPage.getTotalPages());
        m.put("view", "panel_desarrollador/r");
        m.put("estilos", "/css/panelDesarrollador/r.css");
        return "_t/frame";
    }
}
