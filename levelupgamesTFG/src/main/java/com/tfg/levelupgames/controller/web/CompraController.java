package com.tfg.levelupgames.controller.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.UsuarioService;

@Controller
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private UsuarioService usuarioService; // Para buscar usuario por email

    @Autowired
    private JuegoService juegoService; // Por si necesitas info del juego

    @GetMapping("c")
    public String c(@RequestParam Long id, ModelMap m, Principal principal) {
        // Obtener email o username del usuario logeado
        String emailUsuario = principal.getName();

        // Buscar usuario en BD con ese email
        Usuario usuario = usuarioService.findByCorreo(emailUsuario);

        // Si quieres, puedes pasar usuario y juego a la vista
        Juego juego = juegoService.findById(id);

        m.put("view", "compra/c");
        m.put("estilos", "/css/juego/c.css");

        m.put("juego", juego);
        m.put("usuarioId", usuario.getId());
        m.put("juegoId", id);

        return "_t/frame";
    }
}