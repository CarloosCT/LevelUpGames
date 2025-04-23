package org.agaray.pruebas.pap.controller.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
import java.nio.file.Path;

@Controller
@RequestMapping("/imagen")
public class ImagenController 
{

    @Autowired
    private ImagenService imagenService;

    @GetMapping("c")
    public String c(
        ModelMap m
    ) {
        m.put("view","imagen/c");
        m.put("estilos", "/css/home/style.css");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(@RequestParam("imagen") MultipartFile imagen) throws DangerException 
    {
        try {
            String nombreArchivo = imagen.getOriginalFilename();
    
            Path rutaAbsoluta = Paths.get("C:/Users/Usuario/Desktop/LevelUpGames/LevelUpGames/pap2025SP/src/main/resources/static/uploads/" + nombreArchivo);
    
            Files.createDirectories(rutaAbsoluta.getParent());
    
            Files.copy(imagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);
    
            this.imagenService.save(nombreArchivo);
        } catch (IOException e) {
            PRG.error("Error al intentar guardar la imagen: " + e.getMessage(), "/imagen/c");
        } catch (Exception e) {
            PRG.error("Ha ocurrido un error inesperado: " + e.getMessage(), "/imagen/c");
        }
        return "redirect:/panel_administrador/r";
    }

}
