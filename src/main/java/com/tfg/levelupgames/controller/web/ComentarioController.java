package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.services.ComentarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;


    @PostMapping("cpost")
public String cPost(
    @RequestParam("juegoId") Long juegoId,
    @RequestParam("contenido") String contenido,
    HttpSession session,
    ModelMap m) throws DangerException {

    Object userObj = session.getAttribute("user");
    if (userObj == null) {
        throw new DangerException("Debes iniciar sesión para comentar.");
    }

    Usuario usuario = (Usuario) userObj;

    comentarioService.save(juegoId, usuario, contenido);

    return "redirect:/juego/r/" + juegoId;
}


}