package com.tfg.levelupgames.controller.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.JuegoService;

@Controller
@RequestMapping("/juego")
public class JuegoController {
    @Autowired
    private GeneroService generoService;

    @Autowired
    private JuegoService juegoService;

    @GetMapping("c")
    public String c(ModelMap m) {
    m.put("view", "juego/c");
    m.put("generos", generoService.findAll());
    m.put("estilos", "/css/juego/c.css");
    return "_t/frame";
    }

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("juegos", juegoService.findAll());
        m.put("view", "juego/r");
        m.put("estilos", "/css/juego/style.css");
        return "_t/frame";
    }

    @GetMapping("r/{id}")
    public String r(
        @PathVariable Long id,
        ModelMap m) {

    Juego juego = juegoService.findById(id);

    if (juego == null) {
        return "redirect:/juego";
    }

    m.put("juego", juego);
    m.put("view", "juego/r");
    m.put("estilos", "/css/juego/r.css");

    return "_t/frame";
}

    @PostMapping("c")
public String cPost(
    @RequestParam String nombre,
    @RequestParam String descripcion,
    @RequestParam List<Long> generosIds,
    @RequestParam BigDecimal precio,
    @RequestParam("imagenes") MultipartFile[] imagenes,
    @RequestParam("portadaFile") MultipartFile portadaFile,
    @RequestParam("descargable") MultipartFile descargable) throws DangerException {

    if (juegoService.existsByNombre(nombre)) {
        PRG.error("Ya existe un juego con el nombre '" + nombre + "'.", "/juego/c");
    }

    if (precio.compareTo(BigDecimal.ZERO) < 0) {
        PRG.error("El precio no puede ser negativo.", "/juego/c");
    }

    if (portadaFile == null || portadaFile.isEmpty()) {
        PRG.error("Debes subir una imagen de portada.", "/juego/c");
    }

    if (!portadaFile.getContentType().startsWith("image/")) {
        PRG.error("La portada debe ser un archivo de imagen válido.", "/juego/c");
    }

    int imagenesValidas = 0;
    for (MultipartFile imagen : imagenes) {
        if (imagen != null && !imagen.isEmpty()) {
            if (!imagen.getContentType().startsWith("image/")) {
                PRG.error("Todas las imágenes deben ser archivos de imagen válidos.", "/juego/c");
            }
            imagenesValidas++;
        }
    }

    if (imagenesValidas == 0) {
        PRG.error("Debes subir al menos una imagen adicional del juego.", "/juego/c");
    }

    if (imagenesValidas > 5) {
        PRG.error("Puedes subir un máximo de 5 imágenes adicionales.", "/juego/c");
    }

    if (descargable == null || descargable.isEmpty()) {
        PRG.error("Debes subir un archivo descargable del juego.", "/juego/c");
    }

    juegoService.saveJuegoConRelaciones(nombre, descripcion, generosIds, precio, portadaFile, imagenes, descargable);

    return "redirect:/panel_administrador/r";
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

    @GetMapping("/{id}")
    public String verJuego(@PathVariable Long id, ModelMap m) {
        m.put("juego", juegoService.findById(id));
        m.put("view", "juego/r");
        m.put("estilos", "/css/juego/r.css");
        return "_t/frame";
    }

    @PostMapping("d")
    public String d(
            @RequestParam Long id) throws DangerException {
        try {
            juegoService.d(id);
        } catch (Exception e) {
            PRG.error(e.getMessage(), "/juego/r");
        }
        return "redirect:/panel_administrador/r";
    }
}