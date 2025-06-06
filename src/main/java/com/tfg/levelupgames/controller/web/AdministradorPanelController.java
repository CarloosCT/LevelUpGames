package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.ImagenService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.PrecioService;
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/panel_administrador")
public class AdministradorPanelController {
    @Autowired
    private UsuarioService usuarioService;

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

    @GetMapping("/desarrolladores")
    public String listarDesarrolladores(HttpSession session, ModelMap m) {
        Usuario u = (Usuario) session.getAttribute("user");

        if (u == null || !u.isAdmin()) {
            return "redirect:/";
        }

        m.put("desarrolladores", usuarioService.getDesarrolladores());
        m.put("view", "panel_administrador/desarrolladores");
        m.put("estilos", "/css/panelAdministrador/desarrolladores.css");
        return "_t/frame";
    }

    @PostMapping("/quitar_privilegios")
    public String quitarPrivilegios(
            @RequestParam("usuarioId") Long usuarioId,
            HttpSession session,
            ModelMap m) throws DangerException {

        Usuario u = (Usuario) session.getAttribute("user");
        if (u == null || !u.isAdmin()) {
            return "redirect:/";
        }

        if (u.getId().equals(usuarioId)) {
            PRG.error("No puedes quitarte tus propios privilegios.", "/panel_administrador/desarrolladores");
        }

        usuarioService.quitarPrivilegios(usuarioId);
        return "redirect:/panel_administrador/desarrolladores";
    }
}