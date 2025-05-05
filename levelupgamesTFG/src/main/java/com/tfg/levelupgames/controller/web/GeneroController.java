package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.GeneroService;

@Controller
@RequestMapping("/genero")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("generos", generoService.findAll());
        m.put("view", "genero/r");
        m.put("estilos", "/css/genero/style.css");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m) {
        m.put("view", "genero/c");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            this.generoService.save(nombre);
        } catch (Exception e) {
            PRG.error("El género " + nombre + " ya está registrada", "/genero/r");
        }
        return "redirect:/genero/r";
    }

    @PostMapping("d")
    public String d(
            @RequestParam Long id) throws DangerException {
        try {
            generoService.d(id);
        } catch (Exception e) {
            PRG.error(e.getMessage(), "/genero/r");
        }
        return "redirect:/genero/r";
    }

    @GetMapping("u")
    public String u(
            @RequestParam Long id,
            ModelMap m) {
        m.put("genero", generoService.findById(id));
        m.put("estilos", "/css/genero/style.css");
        m.put("view", "genero/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String uPost(
            @RequestParam Long id,
            @RequestParam String nombre) throws DangerException {
        try {
            this.generoService.u(id, nombre);
        } catch (Exception e) {
            PRG.error("El género " + nombre + " ya existe", "/genero/r");
        }
        return "redirect:/genero/r";
    }
}
