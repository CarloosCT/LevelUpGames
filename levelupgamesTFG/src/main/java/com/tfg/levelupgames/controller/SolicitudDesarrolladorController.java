package com.tfg.levelupgames.controller;

import java.util.List;

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
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitud")
public class SolicitudDesarrolladorController {

    @Autowired
    private SolicitudDesarrolladorRepository solicitudRepo;

    @Autowired
    private UsuarioService usuarioService;

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

        List<SolicitudDesarrollador> solicitudesPendientes = solicitudRepo.findByUsuarioAndRevisadaFalse(usuario);
        if (!solicitudesPendientes.isEmpty()) {
            PRG.error("Ya tienes una solicitud pendiente.", "/");
        }

        SolicitudDesarrollador solicitud = new SolicitudDesarrollador();
        solicitud.setUsuario(usuario);
        solicitud.setBiografia(biografia);
        solicitud.setPortfolioUrl(portfolioUrl);
        solicitudRepo.save(solicitud);

        return "redirect:/?success=Solicitud enviada correctamente";
    }
}