package org.agaray.pruebas.pap.controller.web;

import org.agaray.pruebas.pap.exception.DangerException;
import org.agaray.pruebas.pap.helper.PRG;
import org.agaray.pruebas.pap.services.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/genero")
public class GeneroController 
{

    @Autowired
    private GeneroService generoService;

    @GetMapping("c")
    public String c(
        ModelMap m
    ) {
        m.put("view","genero/c");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
        @RequestParam("nombre")
        String nombre
    ) throws DangerException {
        try {
            this.generoService.save(nombre);
        }
        catch (Exception e) {
            PRG.error("El género "+nombre+" ya está registrada","/genero/c");
        }
        return "redirect:/panel_administrador/r";
    }
}
