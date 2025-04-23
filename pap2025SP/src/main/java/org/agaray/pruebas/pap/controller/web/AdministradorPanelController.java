package org.agaray.pruebas.pap.controller.web;

import org.agaray.pruebas.pap.services.GeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panel_administrador")
public class AdministradorPanelController {
    @Autowired
    private GeneroService generoService;

    @GetMapping("r")
    public String r(ModelMap m) {
        m.put("generos", generoService.findAll());
        m.put("view", "panel_administrador/r");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }
}