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
import com.tfg.levelupgames.exception.DangerException;
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
        if (search == null) {
            search = "";
        }

        if (genero == null || genero.isEmpty()) {
            return juegoRepository.findByVisibleTrueAndNombreContainingIgnoreCase(search, pageable);
        } else {
            return juegoRepository.findByVisibleTrueAndNombreContainingIgnoreCaseAndGenerosNombreIgnoreCase(search,
                    genero, pageable);
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
            MultipartFile portadaFile, // Lo ignoramos porque usas multipartFiles + ids
            List<MultipartFile> imagenesFiles,
            MultipartFile descargableFile,
            List<Long> imagenesExistentesIds,
            Long portadaIndex,
            List<Long> imagenesAEliminarIds) throws Exception {

        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new Exception("Juego no encontrado con id: " + id));

        // Actualizar datos básicos
        juego.setNombre(nombre);
        juego.setDescripcion(descripcion);
        juego.setGeneros(generoService.findByIds(generosIds));

        // Actualizar precio: finalizar si cambia, y crear nuevo
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

        // --- ELIMINAR IMÁGENES MARCADAS ---
        if (imagenesAEliminarIds != null) {
            for (Long idImg : imagenesAEliminarIds) {
                Imagen img = imagenService.findById(idImg);
                if (img != null) {
                    cloudinaryService.delete(img.getPublicId(), "image");
                    juego.getImagenes().remove(img);
                    imagenService.eliminarImagenPorId(idImg);
                }
            }
        }

        // --- FILTRAR IMÁGENES EXISTENTES (las que no se eliminaron y están
        // seleccionadas) ---
        List<Imagen> imagenesFiltradas = new ArrayList<>();
        if (imagenesExistentesIds != null && !imagenesExistentesIds.isEmpty()) {
            for (Imagen imagen : juego.getImagenes()) {
                if (imagenesExistentesIds.contains(imagen.getId())) {
                    imagenesFiltradas.add(imagen);
                }
            }
        }

        // --- SUBIR NUEVAS IMÁGENES ---
        List<Imagen> nuevasImagenes = new ArrayList<>();
        if (imagenesFiles != null && !imagenesFiles.isEmpty()) {
            for (MultipartFile file : imagenesFiles) {
                if (file != null && !file.isEmpty()) {
                    Map<String, Object> uploadResult = cloudinaryService.upload(file);
                    String url = (String) uploadResult.get("secure_url");
                    String publicId = (String) uploadResult.get("public_id");

                    Imagen imagen = new Imagen(url, publicId, false);
                    imagen.setJuego(juego);
                    nuevasImagenes.add(imagen);
                }
            }
        }

        // --- LISTA FINAL DE IMÁGENES ---
        List<Imagen> todasLasImagenes = new ArrayList<>();
        todasLasImagenes.addAll(imagenesFiltradas);
        todasLasImagenes.addAll(nuevasImagenes);

        // --- ASIGNAR PORTADA ---
        if (portadaIndex != null) {
            Imagen nuevaPortada = null;

            // Si portadaIndex es índice dentro de nuevasImagenes (portada a imagen nueva)
            if (portadaIndex >= 0 && portadaIndex < nuevasImagenes.size()) {
                nuevaPortada = nuevasImagenes.get(portadaIndex.intValue());
            } else {
                // Si portadaIndex es id de imagen existente, buscar en todasLasImagenes
                for (Imagen img : todasLasImagenes) {
                    if (img.getId() != null && img.getId().equals(portadaIndex)) {
                        nuevaPortada = img;
                        break;
                    }
                }
            }

            if (nuevaPortada == null) {
                throw new Exception("No se encontró la imagen seleccionada como portada.");
            }

            // ELIMINAR PORTADA ANTERIOR SI ES DISTINTA Y YA NO ESTÁ EN LA LISTA
            Imagen portadaAnterior = juego.getPortada();
            if (portadaAnterior != null && !portadaAnterior.equals(nuevaPortada)) {
                // Si la portada anterior no está en la lista final, eliminarla de Cloudinary y
                // BD
                boolean portadaAnteriorEnLista = todasLasImagenes.stream()
                        .anyMatch(img -> img.equals(portadaAnterior));
                if (!portadaAnteriorEnLista) {
                    cloudinaryService.delete(portadaAnterior.getPublicId(), "image");
                    imagenService.eliminarImagenPorId(portadaAnterior.getId());
                }
                portadaAnterior.setPortada(false);
            }

            // Marcar nueva portada
            nuevaPortada.setPortada(true);
            juego.setPortada(nuevaPortada);
        }

        // --- LIMPIAR FLAG PORTADA EN LAS DEMÁS ---
        for (Imagen img : todasLasImagenes) {
            if (juego.getPortada() == null || !img.equals(juego.getPortada())) {
                img.setPortada(false);
            }
        }

        // --- ASIGNAR LAS IMÁGENES AL JUEGO ---
        juego.getImagenes().clear();
        juego.getImagenes().addAll(todasLasImagenes);

        // --- ACTUALIZAR ARCHIVO DESCARGABLE ---
        if (descargableFile != null && !descargableFile.isEmpty()) {
            if (juego.getDescargablePublicId() != null) {
                try {
                    cloudinaryService.delete(juego.getDescargablePublicId(), "raw");
                } catch (IOException e) {
                    System.err.println("No se pudo borrar el descargable previo: " + e.getMessage());
                }
            }
            Map<?, ?> uploadResult = cloudinaryService.upload(descargableFile, Map.of("resource_type", "raw"));
            juego.setDescargable((String) uploadResult.get("secure_url"));
            juego.setDescargablePublicId((String) uploadResult.get("public_id"));
            juego.setNombreDescargableOriginal(descargableFile.getOriginalFilename());
        }

        // Guardar cambios en BD
        juegoRepository.save(juego);
    }

    public void ocultarJuego(Long id) throws DangerException {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new DangerException("Juego no encontrado"));
        juego.setVisible(false);
        juegoRepository.save(juego);
    }

    public void mostrarJuego(Long id) throws DangerException {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new DangerException("Juego no encontrado"));
        juego.setVisible(true);
        juegoRepository.save(juego);
    }

    public List<Juego> findAllVisibles() {
        return juegoRepository.findByVisibleTrue();
    }

}
