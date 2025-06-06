package com.tfg.levelupgames.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.Genero;
import com.tfg.levelupgames.entities.Imagen;
import com.tfg.levelupgames.entities.Juego;
import com.tfg.levelupgames.entities.Precio;
import com.tfg.levelupgames.entities.Usuario;
import com.tfg.levelupgames.repositories.JuegoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Autowired
    private CloudinaryService cloudinaryService;

    public void save(Juego juego) {
        juegoRepository.save(juego);
    }

    public Juego findById(Long id) {
        return juegoRepository.findById(id).orElse(null);
    }

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Page<Juego> findAll(Pageable pageable) {
        return juegoRepository.findAll(pageable);
    }

    public boolean existsByNombre(String nombre) {
        return juegoRepository.existsByNombre(nombre);
    }

    public List<Juego> findByGeneroNombre(String nombre) {
        return juegoRepository.findByGenerosNombre(nombre);
    }

    public Page<Juego> findByDesarrollador(Usuario desarrollador, Pageable pageable) {
        return juegoRepository.findByDesarrollador(desarrollador, pageable);
    }

    public Usuario getDeveloper(Long id) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Juego no encontrado con ID: " + id));
        return juego.getDesarrollador();
    }

    public int contarImagenesExistentes(Long juegoId) {
        Juego juego = juegoRepository.findById(juegoId).orElseThrow();
        return (int) juego.getImagenes()
                .stream()
                .filter(imagen -> !imagen.isPortada())
                .count();
    }

    public boolean isMismoJuego(Long id, String nombre) {
        List<Juego> juegos = juegoRepository.findByNombre(nombre);
        if (juegos.isEmpty()) {
            return true;
        }
        for (Juego juego : juegos) {
            if (!juego.getId().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public Page<Juego> buscarJuegosFiltrados(String search, String genero, Pageable pageable) {
        if ((search == null || search.isBlank()) && (genero == null || genero.isBlank())) {
            return juegoRepository.findAll(pageable);
        } else if (search != null && !search.isBlank() && (genero == null || genero.isBlank())) {
            return juegoRepository.findByNombreContainingIgnoreCase(search, pageable);
        } else if ((search == null || search.isBlank()) && genero != null && !genero.isBlank()) {
            return juegoRepository.findByGenerosNombreIgnoreCase(genero, pageable);
        } else {
            return juegoRepository.findByNombreContainingIgnoreCaseAndGenerosNombreIgnoreCase(search, genero, pageable);
        }
    }

    public void d(Long id) throws Exception {
        Juego juegoABorrar = juegoRepository.findById(id)
                .orElseThrow(() -> new Exception("Juego no encontrado con id: " + id));

        try {
            Precio precioActual = juegoABorrar.getPrecioActual();
            if (precioActual != null) {
                precioActual.setFechaFin(LocalDate.now());
            }

            if (juegoABorrar.getPortada() != null) {
                imagenService.d(juegoABorrar.getPortada().getId());
                juegoABorrar.setPortada(null);
            }

            for (Imagen imagen : new ArrayList<>(juegoABorrar.getImagenes())) {
                imagenService.d(imagen.getId());
            }
            juegoABorrar.getImagenes().clear();

            if (juegoABorrar.getDescargablePublicId() != null && !juegoABorrar.getDescargablePublicId().isEmpty()) {
                try {
                    cloudinaryService.delete(juegoABorrar.getDescargablePublicId(), "raw");
                    juegoABorrar.setDescargable(null);
                    juegoABorrar.setDescargablePublicId(null);
                } catch (IOException e) {
                    System.err.println("Error eliminando descargable en Cloudinary: " + e.getMessage());
                }
            }

            juegoRepository.save(juegoABorrar);
            juegoRepository.delete(juegoABorrar);

        } catch (Exception e) {
            throw new Exception("No se pudo eliminar el juego " + juegoABorrar.getNombre(), e);
        }
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
                Map<?, ?> uploadResult = cloudinaryService.upload(descargable, Map.of("resource_type", "raw"));
                String url = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");

                juego.setDescargable(url);
                juego.setDescargablePublicId(publicId);
                juego.setNombreDescargableOriginal(descargable.getOriginalFilename());

            } catch (IOException e) {
                throw new RuntimeException("Error al subir el archivo descargable a Cloudinary: " + e.getMessage(), e);
            }
        }

        juegoRepository.save(juego);
    }

    public void actualizarJuego(
            Long id,
            String nombre,
            String descripcion,
            List<Long> generosIds,
            BigDecimal nuevoPrecio,
            MultipartFile portadaFile,
            List<MultipartFile> imagenesFiles,
            MultipartFile descargableFile,
            List<Long> imagenesExistentesIds) throws Exception {

        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new Exception("Juego no encontrado con id: " + id));

        juego.setNombre(nombre);
        juego.setDescripcion(descripcion);

        List<Genero> generos = generoService.findByIds(generosIds);
        juego.setGeneros(generos);

        Precio precioActual = juego.getPrecioActual();
        if (precioActual != null && precioActual.getCantidad().compareTo(nuevoPrecio) != 0) {
            precioActual.setFechaFin(LocalDate.now());
        }

        Precio nuevoPrecioEntidad = new Precio();
        nuevoPrecioEntidad.setCantidad(nuevoPrecio);
        nuevoPrecioEntidad.setFechaInicio(LocalDate.now());
        nuevoPrecioEntidad.setFechaFin(null);
        nuevoPrecioEntidad.setJuego(juego);
        juego.setPrecio(nuevoPrecioEntidad);

        if (portadaFile != null && !portadaFile.isEmpty()) {
            if (juego.getPortada() != null) {
                imagenService.eliminarImagenPorId(juego.getPortada().getId());
                juego.setPortada(null);
                juego.getImagenes().removeIf(Imagen::isPortada);
            }

            Map<String, Object> uploadResult = cloudinaryService.upload(portadaFile);
            String url = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            Imagen portada = new Imagen(url, publicId, true);
            portada.setJuego(juego);
            juego.setPortada(portada);
            juego.getImagenes().add(portada);
        }

        if (juego.getImagenes() != null && !juego.getImagenes().isEmpty()) {
            List<Imagen> imagenesAEliminar = new ArrayList<>();
            for (Imagen imagen : juego.getImagenes()) {
                if (!imagen.isPortada()) {
                    if (imagenesExistentesIds == null || !imagenesExistentesIds.contains(imagen.getId())) {
                        imagenesAEliminar.add(imagen);
                    }
                }
            }
            for (Imagen eliminar : imagenesAEliminar) {
                try {
                    cloudinaryService.delete(eliminar.getPublicId(), "image");
                    juego.getImagenes().remove(eliminar);
                    imagenService.eliminarImagenPorId(eliminar.getId());
                } catch (IOException e) {
                    System.err.println("No se pudo borrar imagen antigua: " + e.getMessage());
                }
            }
        }

        if (imagenesFiles != null && !imagenesFiles.isEmpty()) {
            for (MultipartFile file : imagenesFiles) {
                if (file != null && !file.isEmpty()) {
                    Map<String, Object> uploadResult = cloudinaryService.upload(file);
                    String url = (String) uploadResult.get("secure_url");
                    String publicId = (String) uploadResult.get("public_id");

                    Imagen imagen = new Imagen(url, publicId, false);
                    imagen.setJuego(juego);
                    juego.getImagenes().add(imagen);
                }
            }
        }

        if (descargableFile != null && !descargableFile.isEmpty()) {
            if (juego.getDescargablePublicId() != null) {
                try {
                    cloudinaryService.delete(juego.getDescargablePublicId(), "raw");
                } catch (IOException e) {
                    System.err.println("No se pudo borrar el descargable previo: " + e.getMessage());
                }
            }

            Map<?, ?> uploadResult = cloudinaryService.upload(descargableFile, Map.of("resource_type", "raw"));
            String url = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            juego.setDescargable(url);
            juego.setDescargablePublicId(publicId);
            juego.setNombreDescargableOriginal(descargableFile.getOriginalFilename());
        }

        juegoRepository.save(juego);
    }
}
