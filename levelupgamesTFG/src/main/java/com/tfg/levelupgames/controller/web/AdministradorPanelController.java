package com.tfg.levelupgames.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.SolicitudDesarrollador;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.repositories.SolicitudDesarrolladorRepository;
import com.tfg.levelupgames.repositories.UsuarioRepository;
import com.tfg.levelupgames.services.GeneroService;
import com.tfg.levelupgames.services.ImagenService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.PrecioService;
import com.tfg.levelupgames.services.RolService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/panel_administrador")
public class AdministradorPanelController {
    @Autowired
    private GeneroService generoService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private SolicitudDesarrolladorRepository solicitudRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolService rolService;

    @GetMapping("r")
    public String r(HttpSession session, ModelMap m, @RequestParam(required = false) String success,
            @RequestParam(required = false) String error) {
        Usuario u = (Usuario) session.getAttribute("user");

        if (u == null || !u.isAdmin()) {
            return "redirect:/";
        }

        m.put("generos", generoService.findAll());
        m.put("precios", precioService.findAll());
        m.put("imagenes", imagenService.findAll());
        m.put("juegos", juegoService.findAll());
        List<SolicitudDesarrollador> solicitudes = solicitudRepo.findByRevisadaFalse();
        m.put("solicitudes", solicitudes);

        if (success != null) {
            m.put("success", success);
        }
        if (error != null) {
            m.put("error", error);
        }

        m.put("view", "panel_administrador/r");
        m.put("estilos", "/css/panelAdministrador/panel.css");
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

        return "redirect:/panel_administrador/r?success=Solicitud aprobada";
    }

    @PostMapping("rechazar")
    public String rechazarSolicitud(@RequestParam Long id) {
        SolicitudDesarrollador sol = solicitudRepo.findById(id).orElse(null);
        if (sol != null) {
            sol.setAprobada(false);
            sol.setRevisada(true);
            solicitudRepo.save(sol);
        }
        return "redirect:/panel_administrador/r?success=Solicitud rechazada";
    }
}