package com.tfg.levelupgames.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.levelupgames.entities.*;
import com.tfg.levelupgames.repositories.UsuarioRepository;
import com.tfg.levelupgames.services.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class BDinit {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RolService rolService;
    @Autowired
    private GeneroService generoService;
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private ImagenService imagenService;
    @Autowired
    private PrecioService precioService;

    BDinit(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostConstruct
    public void init() {
        if (rolService.findAll().isEmpty()) {
            crearRoles();
        }

        String adminEmail = "admin1@gmail.com";
        if (usuarioRepository.findByLoginemail(adminEmail) == null) {
            crearUsuarioAdmin();
        }

        if (generoService.findAll().isEmpty()) {
            crearGenerosYDesarrolladores();
        }

        if (juegoService.findAll().isEmpty()) {
            crearJuegos();
        }
    }

    private void crearRoles() {
        rolService.save("admin");
        rolService.save("user");
        rolService.save("developer");
    }

    private void crearUsuarioAdmin() {
        Rol adminRol = rolService.findByNombre("admin");
        Usuario admin = new Usuario("admin1@gmail.com", "Admin", "Admin", new BCryptPasswordEncoder().encode("1234"));
        admin.setRol(adminRol);
        usuarioRepository.save(admin);
    }

    private void crearGenerosYDesarrolladores() {
        generoService.save("Aventura");
        generoService.save("Deportes");
    }

    private void crearJuegos() {
        Genero aventura = generoService.findByNombre("Aventura");
        BigDecimal cantidad = new BigDecimal("59.99");
        Usuario admin = usuarioRepository.findByLoginemail("admin1@gmail.com");
        if (admin == null) {
            throw new RuntimeException("Usuario administrador no encontrado al crear juegos");
        }

        crearJuegoConImagenes("Assassin's Creed", "Un juego de aventuras históricas.",
                "Assassin's Creed.iso", List.of(aventura), admin, cantidad,
                "acreed1.jpg", "acreed2.jpg");

        crearJuegoConImagenes("Far Cry", "Acción en mundo abierto en escenarios exóticos.",
                "Far Cry.rar", List.of(aventura), admin, cantidad,
                "farcry1.jpg", "farcry2.jpg");

        crearJuegoConImagenes("Watch Dogs", "Hackea el sistema en esta aventura urbana.",
                "Watch Dogs.exe", List.of(aventura), admin, cantidad,
                "watchdogs1.jpg", "watchdogs2.jpg");

        crearJuegoConImagenes("Prince of Persia", "Aventura mítica con combates y acertijos.",
                "Prince of Persia.setup", List.of(aventura), admin, cantidad,
                "pop1.jpg", "pop2.jpg");
    }

    private void crearJuegoConImagenes(String nombre, String descripcion, String descargable,
            List<Genero> generos, Usuario desarrollador,
            BigDecimal precio, String portadaNombre, String extraNombre) {

        try {
            Juego juego = new Juego();
            juego.setNombre(nombre);
            juego.setDescripcion(descripcion);
            juego.setGeneros(generos);
            juego.setDesarrollador(desarrollador);
            juegoService.save(juego);
            precioService.save(precio, juego);

            Imagen portada = subirImagenDesdeRecursos(portadaNombre, true, juego);
            imagenService.save(portada);
            juego.setPortada(portada);

            Imagen extra = subirImagenDesdeRecursos(extraNombre, false, juego);
            imagenService.save(extra);

            ClassPathResource descargableFile = new ClassPathResource("static/descargables/" + descargable);
            if (!descargableFile.exists()) {
                throw new RuntimeException("Archivo descargable no encontrado: " + descargable);
            }

            MultipartFile multipartDescargable = multipartFileFromResource("static/descargables/" + descargable,
                    "application/octet-stream");

            Map<?, ?> uploadResult = cloudinaryService.upload(multipartDescargable, Map.of("resource_type", "raw"));
            juego.setDescargable((String) uploadResult.get("secure_url"));
            juego.setDescargablePublicId((String) uploadResult.get("public_id"));
            juego.setNombreDescargableOriginal(multipartDescargable.getOriginalFilename());

            juegoService.save(juego);
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imágenes desde recursos", e);
        }
    }

    private Imagen subirImagenDesdeRecursos(String nombreArchivo, boolean esPortada, Juego juego) throws IOException {
        MultipartFile multipartFile = multipartFileFromResource("static/juegos/" + nombreArchivo, "image/jpeg");

        Map<?, ?> uploadResult = cloudinaryService.upload(multipartFile);
        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        Imagen imagen = new Imagen();
        imagen.setRuta(url);
        imagen.setPublicId(publicId);
        imagen.setPortada(esPortada);
        imagen.setJuego(juego);

        return imagen;
    }

    private MultipartFile multipartFileFromResource(String rutaRecurso, String contentType) throws IOException {
        ClassPathResource resource = new ClassPathResource(rutaRecurso);
        byte[] content = resource.getInputStream().readAllBytes();

        String nombreArchivo = resource.getFilename();

        return new MultipartFile() {
            @Override
            public String getName() {
                return nombreArchivo;
            }

            @Override
            public String getOriginalFilename() {
                return nombreArchivo;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return content.length == 0;
            }

            @Override
            public long getSize() {
                return content.length;
            }

            @Override
            public byte[] getBytes() {
                return content;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(content);
            }

            @Override
            public void transferTo(File dest) throws IOException {
                try (OutputStream os = new FileOutputStream(dest)) {
                    os.write(content);
                }
            }
        };
    }
}