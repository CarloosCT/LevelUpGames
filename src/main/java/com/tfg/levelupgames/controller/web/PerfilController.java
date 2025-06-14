package com.tfg.levelupgames.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Compra;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.exception.InfoException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.repositories.CompraRepository;
import com.tfg.levelupgames.repositories.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil")
public class PerfilController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CompraRepository compraRepo;

    @GetMapping("/r")
    public String perfil(
            ModelMap m,
            HttpSession s) throws DangerException {
        Usuario usuario = (Usuario) s.getAttribute("user");
        if (usuario == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }
        m.put("view", "perfil/r");
        m.put("estilos", "/css/perfil/r.css");
        m.put("usuario", usuario);
        return "_t/frame";
    }

    @GetMapping("/verHistorialCompras")
    public String verHistorialCompras(
            ModelMap m,
            HttpSession s,
            @RequestParam(defaultValue = "0") int page) throws DangerException {

        Usuario usuario = (Usuario) s.getAttribute("user");
        if (usuario == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        int pageSize = 4;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("fecha").descending());
        Page<Compra> comprasPage = compraRepo.findByUsuarioId(usuario.getId(), pageable);

        m.put("comprasPage", comprasPage);
        m.put("currentPage", page);
        m.put("totalPages", comprasPage.getTotalPages());

        m.put("view", "perfil/historialCompras");
        m.put("estilos", "/css/perfil/historialCompras.css");
        return "_t/frame";
    }

    @GetMapping("/seguridad")
    public String seguridad(
            ModelMap m,
            HttpSession s) throws DangerException {
        Usuario usuario = (Usuario) s.getAttribute("user");
        if (usuario == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }
        m.put("view", "perfil/seguridad");
        m.put("estilos", "/css/perfil/seguridad.css");
        return "_t/frame";
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            @RequestParam String passwordConfirmacion,
            HttpSession s,
            ModelMap m) throws DangerException, InfoException {

        Usuario usuario = (Usuario) s.getAttribute("user");
        if (usuario == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(passwordActual, usuario.getPassword())) {
            PRG.error("La contraseña actual es incorrecta.", "/perfil/seguridad");
        }

        if (!passwordNueva.equals(passwordConfirmacion)) {
            PRG.error("La nueva contraseña y su confirmación no coinciden.", "/perfil/seguridad");
        }

        String passwordEncriptada = encoder.encode(passwordNueva);
        usuario.setPassword(passwordEncriptada);

        usuarioRepository.save(usuario);
        s.setAttribute("user", usuario);

        PRG.info("Contraseña actualizada correctamente.", "/perfil/r");

        return "redirect:/perfil/r";
    }

    @PostMapping("/actualizarDatosPersonales")
    public String actualizarPerfil(
            @RequestParam String nombre,
            @RequestParam String apellido,
            HttpSession s,
            ModelMap m) throws DangerException {

        Usuario usuario = (Usuario) s.getAttribute("user");

        if (usuario == null) {
            m.put("view", "mensajesInfo/loginError");
            m.put("estilos", "/css/loginError.css");
            return "_t/frame";
        }

        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty()) {
            m.put("view", "mensajesInfo/errorPerfil");
            m.put("estilos", "/css/errorPerfil.css");
            return "_t/frame";
        }

        if (nombre.equals(usuario.getNombre()) && apellido.equals(usuario.getApellido())) {
            PRG.error("No se han realizado cambios en el perfil.", "/perfil/r");
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);

        usuarioRepository.save(usuario);

        s.setAttribute("user", usuario);

        return "redirect:/perfil/r";
    }

}
