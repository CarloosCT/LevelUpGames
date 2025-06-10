package com.tfg.levelupgames.controller.web;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.services.JuegoService;

@Controller
@RequestMapping("/download")
public class DescargaController {
    @Autowired
    private JuegoService juegoService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> descargarJuego(@PathVariable Long id) throws IOException {
        Juego juego = juegoService.findById(id);
        if (juego == null) {
            return ResponseEntity.notFound().build();
        }

        String urlDescargable = juego.getDescargable();
        String nombreArchivo = juego.getNombreDescargableOriginal();

        InputStream inputStream = java.net.URI.create(urlDescargable).toURL().openStream();
        ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
