package com.tfg.levelupgames.controller.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/saldo")
public class SaldoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("r")
    public String r(HttpSession session, ModelMap m) {
        Usuario usuario = usuarioService.obtenerUsuarioActual(session);

        m.put("saldo", usuario.getSaldo());
        m.put("view", "saldo/r");
        m.put("estilos", "/css/saldo/r.css");
        return "_t/frame";
    }

    @PostMapping("add")
    public String añadirSaldo(@RequestParam BigDecimal cantidad, HttpSession session, ModelMap m) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        
        if (usuario == null) {
            m.put("view", "errores/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        usuarioService.sumarSaldo(usuario.getId(), cantidad);

        Usuario usuarioActualizado = usuarioService.findById(usuario.getId());
        session.setAttribute("user", usuarioActualizado);

        return "redirect:/saldo/r";
    }
}
