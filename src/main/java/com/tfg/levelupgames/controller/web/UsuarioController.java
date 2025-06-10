package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.repositories.UsuarioRepository;
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("c")
    public String c(ModelMap m) {
        m.put("view", "usuario/c");
        m.put("estilos", "/css/usuario/c.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam String loginemail,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String password,
            HttpSession s) throws DangerException {
        try {
            Usuario usuario = this.usuarioService.save(loginemail, nombre, apellido, password, "user");
            s.setAttribute("user", usuario);
        } catch (Exception e) {
            PRG.error("Correo " + loginemail + " ya en uso", "/usuario/c");
        }
        return "redirect:/";
    }

    @GetMapping("login")
    public String login(ModelMap m) {
        m.put("estilos", "/css/usuario/login.css");
        m.put("view", "usuario/login");
        return "_t/frame";
    }

    @PostMapping("login")
    public String loginPost(
            @RequestParam String loginemail,
            @RequestParam String password,
            HttpSession s) throws DangerException {
        try {
            Usuario usuario = usuarioService.login(loginemail, password);
            s.setAttribute("user", usuario);

            if (usuario.isPrivilegiosRevocados()) {
                usuario.setPrivilegiosRevocados(false);
                usuarioRepository.save(usuario);
                return "redirect:/usuario/privilegios";
            }
            if (usuario.isMostrarAlertaRechazo()) {
                usuario.setMostrarAlertaRechazo(false);
                usuarioRepository.save(usuario);
                return "redirect:/usuario/rechazo";
            }
            if (usuario.isSolicitudAprobada()) {
                usuario.setSolicitudAprobada(false);
                usuarioRepository.save(usuario);
                return "redirect:/usuario/aprobada";
            }
        } catch (Exception e) {
            PRG.error("Nombre de usuario o contraseña incorrectas", "/usuario/login");
        }
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/";
    }

    @GetMapping("/privilegios")
    public String mostrarPrivilegiosRevocados(ModelMap m) {
        m.put("estilos", "/css/mensajesInfo/privilegios.css");
        m.put("view", "mensajesInfo/privilegios");
        return "_t/frame";
    }

    @GetMapping("/rechazo")
    public String mostrarRechazo(ModelMap m) {
        m.put("estilos", "/css/mensajesInfo/rechazoSolicitud.css");
        m.put("view", "mensajesInfo/rechazoSolicitud");
        return "_t/frame";
    }

    @GetMapping("/aprobada")
    public String mostrarAprobada(ModelMap m) {
        m.put("estilos", "/css/mensajesInfo/solicitudAprobada.css");
        m.put("view", "mensajesInfo/solicitudAprobada");
        return "_t/frame";
    }
}
