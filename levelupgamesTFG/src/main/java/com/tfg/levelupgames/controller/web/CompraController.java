package com.tfg.levelupgames.controller.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.exception.DangerException;
import com.tfg.levelupgames.helper.PRG;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.services.CompraService;
import com.tfg.levelupgames.services.JuegoService;
import com.tfg.levelupgames.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("c/{id}")
    public String c(@PathVariable Long id, ModelMap m, HttpSession session)
            throws DangerException {

        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) {
            PRG.error("Debes iniciar sesión para comprar", "/usuario/login");
        }

        Juego juego = juegoService.findById(id);

        m.put("view", "compra/c");
        m.put("estilos", "/css/compra/c.css");

        m.put("juego", juego);
        m.put("usuarioId", usuario.getId());
        m.put("juegoId", id);

        return "_t/frame";
    }

    @PostMapping("cpost")
    public String cPost(
    @RequestParam("juegoId") Long juegoId,
    HttpSession session, ModelMap m) throws DangerException {

    Usuario usuario = (Usuario) session.getAttribute("user");

    if (usuario == null) {
        PRG.error("Debes iniciar sesión para realizar la compra", "/usuario/login");
    }

    Juego juego = null;
    BigDecimal precioJuego = null;

    try {
        juego = juegoService.findById(juegoId);
        BigDecimal saldoActual = usuario.getSaldo();
        precioJuego = juego.getPrecio().getCantidad();

        if (compraService.existeCompra(usuario, juego)) {
            PRG.error("Ya has comprado este juego", "/");
        }

        if (saldoActual.compareTo(precioJuego) < 0) {
            m.put("mensaje", "No tienes saldo suficiente para comprar este juego.");
            m.put("homeUrl", "/");
            m.put("saldoUrl", "/saldo/r");
            m.put("view", "compra/errorSaldo");
            m.put("estilos", "/css/compra/errorSaldo.css");
            return "_t/frame";
        }

        usuario.setSaldo(saldoActual.subtract(precioJuego));
        usuarioService.save(usuario);

        compraService.save(juego, usuario);

    } catch (Exception e) {
        PRG.error("No se pudo realizar la compra: " + e.getMessage(), "/");
    }

    m.put("juego", juego);
    m.put("precio", precioJuego);
    m.put("view", "compra/confirmacion");
    m.put("estilos", "/css/compra/confirmacion.css");
    return "_t/frame";
}

}