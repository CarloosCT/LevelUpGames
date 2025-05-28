package com.tfg.levelupgames.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Genero;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.repositories.JuegoRepository;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private ImagenService imagenService;

    public void save(Juego juego) {
        juegoRepository.save(juego);
    }

    public void saveJuegoConRelaciones(
            String nombre,
            String descripcion,
            List<Long> generosIds,
            BigDecimal precio,
            MultipartFile portadaFile,
            MultipartFile[] imagenes,
            MultipartFile descargable,
            Usuario desarrollador) {

        List<Genero> generos = generoService.findByIds(generosIds);
        Juego juego = new Juego(nombre, descripcion, generos, new ArrayList<>(), new ArrayList<>(), null);
        juego.setDesarrollador(desarrollador);
        juego = juegoRepository.save(juego);

        precioService.save(precio, juego);
        Precio precioActual = precioService.findByJuegoCantidadFechaFinNull(juego, precio);
        juego.setPrecio(precioActual);

        imagenService.procesarImagenesDeJuego(juego, portadaFile, imagenes);

        if (descargable != null && !descargable.isEmpty()) {
            try {
                Path downloadDir = Paths.get("downloadables");
                if (!Files.exists(downloadDir)) {
                    Files.createDirectories(downloadDir);
                }

                String nombreOriginal = descargable.getOriginalFilename();
                String extension = "";

                int i = nombreOriginal.lastIndexOf('.');
                if (i > 0) {
                    extension = nombreOriginal.substring(i);
                }

                String nombreArchivoUnico = UUID.randomUUID().toString() + extension;
                Path rutaArchivo = downloadDir.resolve(nombreArchivoUnico);

                Files.copy(descargable.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
                juego.setDescargable(nombreArchivoUnico);

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo descargable: " + e.getMessage(), e);
            }
        }

        juegoRepository.save(juego);
    }

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Juego findById(Long id) {
        return juegoRepository.findById(id).orElse(null);
    }

    public void d(Long id) throws Exception {
        Juego juegoABorrar = juegoRepository.findById(id).get();
        try {
            juegoRepository.deleteById(id);
            juegoABorrar.getImagenes().forEach(imagen -> {
                try {
                    imagenService.d(imagen.getId());
                    String ruta = imagen.getRuta();
                    Path path = Paths.get("uploads/" + ruta);
                    path.toFile().delete();
                } catch (Exception e) {
                    throw new RuntimeException("Error al eliminar la imagen: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            throw new Exception("No se pudo eliminar el juego " + juegoABorrar.getNombre());
        }
    }

    public List<Juego> findByGeneroNombre(String nombre) {
        return juegoRepository.findByGenerosNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return juegoRepository.existsByNombre(nombre);
    }

    public List<Juego> findByDesarrollador(Usuario desarrollador) {
        return juegoRepository.findByDesarrollador(desarrollador);
    }

    public boolean isMismoJuego(Long id, String nombre) {
    List<Juego> juegos = juegoRepository.findByNombre(nombre);
    if (juegos.isEmpty()) {
        // No hay juegos con ese nombre => no hay conflicto
        return true;
    }
    // Verifica si alguno de esos juegos tiene un ID diferente
    for (Juego juego : juegos) {
        if (!juego.getId().equals(id)) {
            // Encontró otro juego distinto con el mismo nombre
            return false;
        }
    }
    // Todos los juegos encontrados tienen el mismo ID que el pasado => es el mismo juego
    return true;
}

    public int contarImagenesExistentes(Long juegoId) {
        Optional<Juego> optionalJuego = juegoRepository.findById(juegoId);
        if (optionalJuego.isEmpty()) {
            return 0;
        }
        Juego juego = optionalJuego.get();
        return (int) juego.getImagenes()
                .stream()
                .filter(imagen -> imagen != null && imagen.getRuta() != null && !imagen.getRuta().isEmpty())
                .count();
    }

    public void modificar(
        Long id,
        String nombre,
        String descripcion,
        List<Long> generosIds,
        BigDecimal precio,
        MultipartFile portadaFile,
        List<MultipartFile> imagenes,
        String imagenesEliminadas,
        MultipartFile descargable) {

    Juego juego = juegoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Juego no encontrado con id " + id));

    juego.setNombre(nombre);
    juego.setDescripcion(descripcion);

    List<Genero> generos = generoService.findByIds(generosIds);
    juego.setGeneros(generos);

    juego = juegoRepository.save(juego);

    precioService.save(precio, juego);
    Precio precioActual = precioService.findByJuegoCantidadFechaFinNull(juego, precio);
    juego.setPrecio(precioActual);

    if (imagenesEliminadas != null && !imagenesEliminadas.isBlank()) {
        String[] idsEliminar = imagenesEliminadas.split(",");
        for (String imgIdStr : idsEliminar) {
            if (imgIdStr.isBlank())
                continue;
            Long imgId = Long.parseLong(imgIdStr.trim());
            imagenService.eliminarImagenPorId(imgId);
        }
    }

    MultipartFile[] imagenesArray = (imagenes != null) ? imagenes.toArray(new MultipartFile[0]) : new MultipartFile[0];
    imagenService.procesarImagenesDeJuego(juego, portadaFile, imagenesArray);

    if (descargable != null && !descargable.isEmpty()) {
        try {
            Path downloadDir = Paths.get("downloadables");
            if (!Files.exists(downloadDir)) {
                Files.createDirectories(downloadDir);
            }

            String nombreOriginal = descargable.getOriginalFilename();
            String extension = "";

            int i = nombreOriginal.lastIndexOf('.');
            if (i > 0) {
                extension = nombreOriginal.substring(i);
            }

            String nombreArchivoUnico = UUID.randomUUID().toString() + extension;
            Path rutaArchivo = downloadDir.resolve(nombreArchivoUnico);

            Files.copy(descargable.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            juego.setDescargable(nombreArchivoUnico);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo descargable: " + e.getMessage(), e);
        }
    }

    juegoRepository.save(juego);
}
}
