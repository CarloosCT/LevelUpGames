package com.tfg.levelupgames.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
    @GetMapping("/r")
    public String perfil(
            ModelMap m,
            HttpSession s) throws DangerException {
        Usuario usuario = (Usuario) s.getAttribute("user");
        if (usuario == null) {
            PRG.error("Debes iniciar sesión para ver tu perfil", "/usuario/login");
        }
        m.put("view", "usuario/perfil");
        m.put("estilos", "/css/usuario/perfil.css");
        m.put("usuario", usuario);
        return "_t/frame";
    }
}
