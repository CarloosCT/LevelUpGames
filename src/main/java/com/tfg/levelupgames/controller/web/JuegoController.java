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
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.JuegoService;

import jakarta.servlet.http.HttpSession;

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
    @RequestParam("descargable") MultipartFile descargable,
    HttpSession session) throws DangerException {

    Usuario desarrollador = (Usuario) session.getAttribute("user");

    if (desarrollador == null) {
        PRG.error("Debes iniciar sesión para crear un juego.", "/usuario/login");
    }

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

    juegoService.saveJuegoConRelaciones(
        nombre, descripcion, generosIds, precio,
        portadaFile, imagenes, descargable,
        desarrollador
    );

    return "redirect:/panel_desarrollador/r";
}


    @GetMapping("u/{id}")
    public String u(@PathVariable Long id, ModelMap m) {
        
    m.put("juego", juegoService.findById(id));
    m.put("generos", generoService.findAll());
    m.put("estilos", "/css/juego/u.css");
    m.put("view", "juego/u");

    return "_t/frame";
    }

    @PostMapping("u")
public String uPost(
        @RequestParam Long id,
        @RequestParam String nombre,
        @RequestParam BigDecimal precio,
        @RequestParam String descripcion,
        @RequestParam List<Long> generosIds,
        @RequestParam(required = false) MultipartFile portadaFile,
        @RequestParam(required = false) List<MultipartFile> imagenes,
        @RequestParam(required = false) String imagenesEliminadas,
        @RequestParam(required = false) MultipartFile descargable
) throws DangerException {

    // Validaciones básicas obligatorias (ID, nombre, precio, desc, géneros)
    if (id == null)
        PRG.error("ID del juego es obligatorio.", "/juego/u?id=" + id);
    if (nombre == null || nombre.trim().isEmpty())
        PRG.error("El nombre es obligatorio.", "/juego/u?id=" + id);
    if (precio == null)
        PRG.error("El precio es obligatorio.", "/juego/u?id=" + id);
    if (descripcion == null || descripcion.trim().isEmpty())
        PRG.error("La descripción es obligatoria.", "/juego/u?id=" + id);
    if (generosIds == null || generosIds.isEmpty())
        PRG.error("Debes seleccionar al menos un género.", "/juego/u?id=" + id);

    // Validar existencia y nombre único
    if (juegoService.existsByNombre(nombre) && !juegoService.isMismoJuego(id, nombre))
        PRG.error("Ya existe un juego con el nombre '" + nombre + "'.", "/juego/u?id=" + id);

    if (precio.compareTo(BigDecimal.ZERO) < 0)
        PRG.error("El precio no puede ser negativo.", "/juego/u?id=" + id);

    // VALIDACIÓN PORTADA
    if (portadaFile != null && !portadaFile.isEmpty()) {
        if (!portadaFile.getContentType().startsWith("image/"))
            PRG.error("La portada debe ser un archivo de imagen válido.", "/juego/u?id=" + id);
        // Se usará el archivo nuevo
    } 
    // Si portadaFile es null o está vacía, se mantiene la portada existente

    // VALIDACIÓN IMÁGENES ADICIONALES
    int imagenesValidas = 0;
    if (imagenes != null && !imagenes.isEmpty()) {
        for (MultipartFile imagen : imagenes) {
            if (imagen != null && !imagen.isEmpty()) {
                if (!imagen.getContentType().startsWith("image/"))
                    PRG.error("Todas las imágenes deben ser archivos de imagen válidos.", "/juego/u?id=" + id);
                imagenesValidas++;
            }
        }
    }
    // Si no hay imágenes nuevas (imagenes null o vacía), se mantienen las existentes

    // Validar número total imágenes después de eliminar las indicadas
    int actuales = juegoService.contarImagenesExistentes(id);
    int eliminadasCount = 0;
    if (imagenesEliminadas != null && !imagenesEliminadas.trim().isEmpty()) {
        eliminadasCount = imagenesEliminadas.split(",").length;
    }
    int totalDespues = actuales - eliminadasCount + imagenesValidas;

    if (totalDespues < 1)
        PRG.error("Debe haber al menos una imagen adicional del juego.", "/juego/u?id=" + id);

    if ((imagenesValidas > 0 || eliminadasCount > 0) && totalDespues > 5)
        PRG.error("Puedes subir un máximo de 5 imágenes adicionales.", "/juego/u?id=" + id);

    // VALIDACIÓN DESCARGABLE
    if (descargable != null && !descargable.isEmpty()) {
        String tipo = descargable.getContentType();
        if (!(tipo.startsWith("application/") || tipo.startsWith("application/x-"))) 
            PRG.error("El archivo descargable no es válido.", "/juego/u?id=" + id);
        // Se usará el nuevo archivo
    } 
    // Si descargable es null o vacío, se mantiene el archivo descargable existente

    // Finalmente llamar al servicio para modificar el juego,
    // pasando los archivos que pueden ser null o vacíos
    juegoService.modificar(
            id, nombre, descripcion, generosIds, precio,
            portadaFile, imagenes, imagenesEliminadas, descargable
    );

    return "redirect:/panel_desarrollador/r";
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