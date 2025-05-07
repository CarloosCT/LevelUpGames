package com.tfg.levelupgames.controller.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.PrecioService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/juego")
public class JuegoController 
{
    @Autowired
    private GeneroService generoService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private JuegoService juegoService;

    @GetMapping("c")
    public String c(
        ModelMap m,
        @SessionAttribute(required = false) String nombreJuego,
        @SessionAttribute(required = false) List<Long> generosSeleccionados,
        @SessionAttribute(required = false) Double precioSeleccionado
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
        @RequestParam String nombre,
        @RequestParam List<Long> generosIds,
        @RequestParam Long precioId,
        @RequestParam MultipartFile[] imagenes,
        HttpSession session
    ) throws DangerException {
        try {

            if (imagenes.length == 0) {
                PRG.error("No se han seleccionado imágenes.", "/juego/c");
                return "redirect:/juego/c";
            }
    
            if (imagenes.length > 5) {
                PRG.error("Puedes subir un máximo de 5 imágenes.", "/juego/c");
                return "redirect:/juego/c";
            }
    
            juegoService.saveJuegoConRelaciones(nombre, generosIds, precioId, imagenes);
    
            session.setAttribute("nombreJuego", nombre);
            session.setAttribute("generosSeleccionados", generosIds);
            session.setAttribute("precioSeleccionado", precioId);
    
        } catch (Exception e) {
            PRG.error("Ha ocurrido un error inesperado: " + e.getMessage(), "/juego/c");
        }
    
        return "redirect:/panel_administrador/r";
    }

    @PostMapping("d")
    public String d(
            @RequestParam Long id) throws DangerException {
        try {
            juegoService.d(id);
        } catch (Exception e) {
            PRG.error(e.getMessage(), "/juego/r");
        }
        return "redirect:/juego/r";
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