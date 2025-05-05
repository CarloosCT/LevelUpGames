package org.agaray.pruebas.pap.controller.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;

import org.agaray.pruebas.pap.entities.Imagen;
import org.agaray.pruebas.pap.exception.DangerException;
import org.agaray.pruebas.pap.helper.PRG;
import org.agaray.pruebas.pap.services.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/imagen")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping("r")
    public String r(ModelMap m) {
        m.put("imagenes", imagenService.findAll());
        m.put("view", "imagen/r");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @GetMapping("d")
    public String d(@RequestParam Long id) throws DangerException {
        try {
            imagenService.d(id);
        } catch (Exception e) {
            PRG.error("No se pudo eliminar la imagen: " + e.getMessage(), "/imagen/r");
        }
        return "redirect:/imagen/r";
    }

    @GetMapping("u")
    public String u(@RequestParam Long id, ModelMap m) {
        m.put("imagen", imagenService.findById(id));
        m.put("view", "imagen/u");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @PostMapping("u")
    public String uPost(@RequestParam Long id,
                        @RequestParam("imagen") MultipartFile nuevaImagen) throws DangerException {
        try {
            String nombreArchivo = nuevaImagen.getOriginalFilename();
            Path rutaAbsoluta = Paths.get("src/main/resources/static/uploads/" + nombreArchivo);
            Files.createDirectories(rutaAbsoluta.getParent());
            Files.copy(nuevaImagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);

            imagenService.u(id, nombreArchivo);
        } catch (IOException e) {
            PRG.error("Error al actualizar la imagen: " + e.getMessage(), "/imagen/u?id=" + id);
        }

        return "redirect:/imagen/r";
    }
}
