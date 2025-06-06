package com.tfg.levelupgames.controller.web;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.ReporteComentario;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.repositories.ComentarioRepository;
import com.tfg.levelupgames.repositories.ReporteComentarioRepository;
import com.tfg.levelupgames.services.ComentarioService;
import com.tfg.levelupgames.services.CompraService;
import com.tfg.levelupgames.services.JuegoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private ReporteComentarioRepository reporteComentarioRepository;

    @PostMapping("cpost")
    public String cPost(
            @RequestParam("juegoId") Long juegoId,
            @RequestParam("contenido") String contenido,
            HttpSession session,
            ModelMap m) throws DangerException {

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        Usuario usuario = (Usuario) userObj;

        if (!compraService.existeCompra(usuario, juegoService.findById(juegoId))) {
            throw new DangerException("Solo puedes comentar juegos que has comprado.");
        }

        comentarioService.save(juegoId, usuario, contenido);

        return "redirect:/juego/r/" + juegoId;
    }

    @PostMapping("/reportar")
    public String reportarComentario(
            @RequestParam("comentarioId") Long comentarioId,
            @RequestParam("motivo") String motivo,
            HttpSession session,
            ModelMap m) {

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        Usuario usuario = (Usuario) userObj;

        try {
            comentarioService.reportarComentario(comentarioId, usuario, motivo);
        } catch (Exception e) {
            m.put("error", e.getMessage());
        }

        // Redirigir a la página del juego o a donde quieras volver
        Long juegoId = comentarioService.getJuegoIdByComentarioId(comentarioId);
        return "redirect:/juego/r/" + juegoId;
    }

    @GetMapping("/reportar/form")
    public String mostrarFormularioReporte(
            @RequestParam("comentarioId") Long comentarioId,
            @RequestParam("juegoId") Long juegoId,
            HttpSession session,
            ModelMap m) {

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        m.put("comentarioId", comentarioId);
        m.put("juegoId", juegoId);
        m.put("view", "comentario/reportar");
        m.put("estilos", "/css/comentario/report.css");
        return "_t/frame";
    }

    @PostMapping("/reportar/enviar")
    public String enviarReporteComentario(
            @RequestParam("comentarioId") Long comentarioId,
            @RequestParam("juegoId") Long juegoId,
            @RequestParam("motivo") String motivo,
            HttpSession session,
            ModelMap m) throws DangerException {

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        Usuario usuario = (Usuario) userObj;

        try {
            comentarioService.reportarComentario(comentarioId, usuario, motivo);
        } catch (Exception e) {
            PRG.error(e.getMessage(), "/juego/r/" + juegoId);
        }

        return "redirect:/juego/r/" + juegoId;
    }

    @GetMapping("/reportes")
    public String verReportesComentarios(ModelMap m, HttpSession session) throws DangerException {
        Usuario u = (Usuario) session.getAttribute("user");

        if (u == null || !u.isAdmin()) {
            PRG.error("Acceso denegado. Solo los administradores pueden ver los reportes.", "/");
        }

        List<ReporteComentario> reportes = reporteComentarioRepository.findAllByOrderByFechaDesc();
        m.put("reportes", reportes);
        m.put("estilos", "/css/panelAdministrador/reportes.css");
        m.put("view", "panel_administrador/reportes");
        return "_t/frame";
    }

    @PostMapping("/reportes/eliminar")
    public String eliminarReporteYComentario(
            @RequestParam("reporteId") Long reporteId,
            HttpSession session,
            ModelMap m) throws DangerException {

        Usuario u = (Usuario) session.getAttribute("user");

        if (u == null || !u.isAdmin()) {
            PRG.error("Acceso denegado. Solo los administradores pueden eliminar reportes.", "/");
        }

        try {
            // Buscar el reporte primero
            Optional<ReporteComentario> optionalReporte = reporteComentarioRepository.findById(reporteId);

            if (optionalReporte.isPresent()) {
                ReporteComentario reporte = optionalReporte.get();

                // Primero eliminar el comentario asociado
                comentarioRepository.delete(reporte.getComentario());

                // Luego eliminar el reporte
                reporteComentarioRepository.delete(reporte);
            } else {
                PRG.error("El reporte no existe.", "/comentario/reportes");
            }

        } catch (Exception e) {
            PRG.error("Error al eliminar el reporte y comentario: " + e.getMessage(), "/comentario/reportes");
        }

        return "redirect:/comentario/reportes";
    }
}