package com.tfg.levelupgames.controller.web;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.tfg.levelupgames.services.ComentarioService;
import com.tfg.levelupgames.services.CompraService;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.ValoracionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/juego")
public class JuegoController {

    @Autowired
    private GeneroService generoService;

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ValoracionService valoracionService;

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
            HttpSession session,
            ModelMap m) {

        Juego juego = juegoService.findById(id);

        if (juego == null) {
            return "redirect:/juego";
        }

        Usuario usuario = (Usuario) session.getAttribute("user");

        Float valoracionMedia = juego.getValoracionMedia();
        Float valoracionUsuario = valoracionService.obtenerValoracionDeUsuario(usuario, juego);

        m.put("juego", juego);
        m.put("user", usuario);
        m.put("valoracionMedia", valoracionMedia);
        m.put("tieneJuego", compraService.existeCompra(usuario, juego));
        m.put("comentarios", comentarioService.getComentariosPorJuegoId(id));
        m.put("developer", juegoService.getDeveloper(id));
        m.put("valoracionUsuario", valoracionUsuario);
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
            @RequestParam("portadaIndex") int portadaIndex,
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

        if (imagenes == null || imagenes.length == 0) {
            PRG.error("Debes seleccionar al menos una imagen.", "/juego/c");
        }

        if (portadaIndex < 0 || portadaIndex >= imagenes.length) {
            PRG.error("Selecciona una imagen válida como portada.", "/juego/c");
        }

        // Extraer la portada
        MultipartFile portadaFile = imagenes[portadaIndex];

        // Validaciones de portada
        if (portadaFile == null || portadaFile.isEmpty()) {
            PRG.error("La imagen de portada no es válida.", "/juego/c");
        }

        if (!portadaFile.getContentType().startsWith("image/")) {
            PRG.error("La portada debe ser un archivo de imagen válido.", "/juego/c");
        }

        // Filtrar las imágenes para excluir la portada
        List<MultipartFile> otrasImagenes = new ArrayList<>();
        for (int i = 0; i < imagenes.length; i++) {
            MultipartFile imagen = imagenes[i];
            if (i != portadaIndex && imagen != null && !imagen.isEmpty()) {
                if (imagen.getContentType() == null || !imagen.getContentType().startsWith("image/")) {
                    PRG.error("Todas las imágenes deben ser archivos de imagen válidos.", "/juego/c");
                }
                otrasImagenes.add(imagen);
            }
        }

        int imagenesValidas = otrasImagenes.size();

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
                portadaFile, otrasImagenes.toArray(new MultipartFile[0]), descargable,
                desarrollador);

        return "redirect:/panel_desarrollador/r";
    }

    @GetMapping("u")
    public String u(ModelMap m,
            @RequestParam Long id,
            HttpSession session) throws DangerException {

        Usuario desarrollador = (Usuario) session.getAttribute("user");

        if (desarrollador == null) {
            PRG.error("Debes iniciar sesión para Modificar el juego.", "/usuario/login");
        }

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
            @RequestParam(required = false) MultipartFile descargable,
            @RequestParam(required = false) Long portadaIndex,
            @RequestParam(required = false) List<Long> imagenesAEliminar,
            @RequestParam(required = false) List<Long> imagenesExistentes) throws DangerException {

        try {
            if (portadaIndex != null && imagenesAEliminar != null && imagenesAEliminar.contains(portadaIndex)) {
                PRG.error("No puedes eliminar la portada del juego.", "/juego/u?id=" + id);
            }
            juegoService.actualizarJuego(
                    id,
                    nombre,
                    descripcion,
                    generosIds,
                    precio,
                    portadaFile,
                    imagenes,
                    descargable,
                    imagenesExistentes,
                    portadaIndex,
                    imagenesAEliminar);
        } catch (Exception e) {
            PRG.error(e.getMessage(), "/juego/u?id=" + id);
        }

        return "redirect:/panel_desarrollador/r";
    }

    @GetMapping("/{id}")
    public String verJuego(@PathVariable Long id, ModelMap m) {
        m.put("juego", juegoService.findById(id));
        m.put("view", "juego/r");
        m.put("estilos", "/css/juego/r.css");
        return "_t/frame";
    }

    @PostMapping("/visibilidad")
    public String cambiarVisibilidad(@RequestParam Long id, @RequestParam boolean visible) throws DangerException {
        if (visible) {
            juegoService.ocultarJuego(id);
        } else {
            juegoService.mostrarJuego(id);
        }
        return "redirect:/panel_desarrollador/r";
    }
}