package org.agaray.pruebas.pap.controller.web;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.agaray.pruebas.pap.exception.DangerException;
import org.agaray.pruebas.pap.helper.PRG;
import org.agaray.pruebas.pap.services.PrecioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/precio")
public class PrecioController 
{

    @Autowired
    private PrecioService precioService;

    @GetMapping("c")
    public String c(
        ModelMap m
    ) {
        m.put("view","precio/c");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
    @RequestParam("precio") Double precio
    )   throws DangerException {
    try{
        BigDecimal precioDecimal = new BigDecimal(precio).setScale(2, RoundingMode.HALF_UP);
        Double precioNormalizado = precioDecimal.doubleValue();

        this.precioService.save(precioNormalizado);
    } catch (Exception e) {
        PRG.error("El precio " + precio + " ya está registrado", "/precio/c");
    }
    return "redirect:/panel_administrador/r";
}
}
