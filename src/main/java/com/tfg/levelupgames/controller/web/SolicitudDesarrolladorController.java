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

import com.tfg.levelupgames.entities.SolicitudDesarrollador;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.repositories.SolicitudDesarrolladorRepository;
import com.tfg.levelupgames.repositories.UsuarioRepository;
import com.tfg.levelupgames.services.RolService;
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitud")
public class SolicitudDesarrolladorController {

    @Autowired
    private SolicitudDesarrolladorRepository solicitudRepo;

    @Autowired
    private UsuarioService usuarioService;
   
    @Autowired
    private UsuarioRepository usuarioRepo;
   
    @Autowired
    private RolService rolService;
    

    @GetMapping("c")
    public String mostrarFormulario(ModelMap m) {
        m.put("estilos", "/css/solicitud/c.css");
        m.put("view", "solicitud/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String enviarSolicitud(
            @RequestParam String biografia,
            @RequestParam String portfolioUrl,
            HttpSession session) throws DangerException {

        Usuario usuario = usuarioService.obtenerUsuarioActual(session);

        // Buscar la última solicitud del usuario (asumiendo solo una a la vez)
        Optional<SolicitudDesarrollador> solicitudExistenteOpt = solicitudRepo.findByUsuario(usuario);

        if (solicitudExistenteOpt.isPresent()) {
            SolicitudDesarrollador solicitudExistente = solicitudExistenteOpt.get();

            if (!solicitudExistente.isRevisada()) {
                PRG.error("Ya tienes una solicitud pendiente.", "/");
            }

            if (solicitudExistente.isAprobada()) {
                PRG.error("Ya has sido aprobado como desarrollador.", "/");
            }

            // Si está revisada y rechazada, eliminamos la anterior
            solicitudRepo.delete(solicitudExistente);
        }

        // Crear nueva solicitud
        SolicitudDesarrollador nuevaSolicitud = new SolicitudDesarrollador();
        nuevaSolicitud.setUsuario(usuario);
        nuevaSolicitud.setBiografia(biografia);
        nuevaSolicitud.setPortfolioUrl(portfolioUrl);
        solicitudRepo.save(nuevaSolicitud);

        return "redirect:/?success=Solicitud enviada correctamente";
    }

    @GetMapping("r")
    public String r(HttpSession session, ModelMap m, @RequestParam(required = false) String success,
            @RequestParam(required = false) String cancel) {
        Usuario u = (Usuario) session.getAttribute("user");

        if (u == null || !u.isAdmin()) {
            return "redirect:/";
        }

        List<SolicitudDesarrollador> solicitudes = solicitudRepo.findByRevisadaFalse();
        m.put("solicitudes", solicitudes);

        if (success != null) {
            m.put("success", success);
        }
        if (cancel != null) {
            m.put("cancel", cancel);
        }

        m.put("view", "solicitud/r");
        m.put("estilos", "/css/solicitud/r.css");
        return "_t/frame";
    }

    @PostMapping("aprobar")
    public String aprobarSolicitud(@RequestParam Long id) {
        SolicitudDesarrollador solicitud = solicitudRepo.findById(id).orElseThrow();
        solicitud.setAprobada(true);
        solicitud.setRevisada(true);
        solicitudRepo.save(solicitud);

        Usuario usuario = solicitud.getUsuario();
        usuario.setRol(rolService.findByNombre("developer"));
        usuarioRepo.save(usuario);

        return "redirect:/solicitud/r?success=Solicitud aprobada";
    }

    @PostMapping("rechazar")
    public String rechazarSolicitud(@RequestParam Long id) {
        SolicitudDesarrollador sol = solicitudRepo.findById(id).orElse(null);
        if (sol != null) {
            sol.setAprobada(false);
            sol.setRevisada(true);

            Usuario usuario = sol.getUsuario();
            usuario.setMostrarAlertaRechazo(true);

            usuarioRepo.save(usuario);
            solicitudRepo.save(sol);
        }
        return "redirect:/solicitud/r?success=Solicitud rechazada";
    }
}