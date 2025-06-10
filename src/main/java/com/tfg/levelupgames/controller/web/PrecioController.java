package com.tfg.levelupgames.controller.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.PrecioService;

@Controller
@RequestMapping("/precio")
public class PrecioController {

    @Autowired
    private PrecioService precioService;

    @Autowired
    private JuegoService juegoService;

    @GetMapping("r")
    public String r(ModelMap m) {
        m.put("precios", precioService.findAll());
        m.put("view", "precio/r");
        m.put("estilos", "/css/precio/c.css");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(ModelMap m) {
        m.put("view", "precio/c");
        m.put("estilos", "/css/precio/c.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(@RequestParam Long juegoId, @RequestParam BigDecimal cantidad) throws DangerException {
        try {
            Juego juego = juegoService.findById(juegoId);
            if (juego == null) {
                PRG.error("Juego no encontrado", "/precio/r");
            }

            precioService.save(cantidad, juego);

        } catch (Exception e) {
            PRG.error("Error al guardar el precio: " + e.getMessage(), "/precio/r");
        }
        return "redirect:/precio/r";
    }
}
