package org.agaray.pruebas.pap.controller.web;

import java.util.ArrayList;
import java.util.List;

import org.agaray.pruebas.pap.exception.DangerException;
import org.agaray.pruebas.pap.helper.PRG;
import org.agaray.pruebas.pap.services.GeneroService;
import org.agaray.pruebas.pap.services.PrecioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/juego")
public class JuegoController 
{
    @Autowired
    private GeneroService generoService;

    @Autowired
    private PrecioService precioService;

    @GetMapping("c")
    public String c(
        ModelMap m,
        @SessionAttribute(value = "nombreJuego", required = false) String nombreJuego,
        @SessionAttribute(value = "generosSeleccionados", required = false) List<Long> generosSeleccionados,
        @SessionAttribute(value = "precioSeleccionado", required = false) Double precioSeleccionado
    ) {
        m.put("view", "juego/c");
        m.put("generos", generoService.findAll());
        m.put("precios", precioService.findAll());
        m.put("nombreJuego", nombreJuego != null ? nombreJuego : "");  // Mantener el nombre del juego
        m.put("generosSeleccionados", generosSeleccionados != null ? generosSeleccionados : new ArrayList<>());
        m.put("precioSeleccionado", precioSeleccionado != null ? precioSeleccionado : null);
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
        @RequestParam("nombre") String nombre,
        @RequestParam(value = "generosIds", required = false) List<Long> generosIds,
        @RequestParam("precioId") Long precioId,
        HttpSession session
    ) throws DangerException {
        try {
            // Guardar los datos del juego en la sesión
            session.setAttribute("nombreJuego", nombre);
            session.setAttribute("generosSeleccionados", generosIds);
            session.setAttribute("precioSeleccionado", precioService.findById(precioId).getCantidad());

            // Guardar el juego en la base de datos
            this.generoService.save(nombre);
        } catch (Exception e) {
            PRG.error("El género " + nombre + " ya está registrado", "/genero/c");
        }
        return "redirect:/panel_administrador/r";
    }
}