package org.agaray.pruebas.pap.init;

import java.util.ArrayList;

import org.agaray.pruebas.pap.services.AficionService;
import org.agaray.pruebas.pap.services.PaisService;
import org.agaray.pruebas.pap.services.PersonaService;
import org.agaray.pruebas.pap.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class BDinit {

    @Autowired
    private PaisService paisService;

    @Autowired
    private AficionService aficionService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PersonaService personaService;

    @PostConstruct
    public void init() {
        if (rolService.r().size() == 0) {
            crearPaises();
            crearAficiones();
            crearRoles();
            crearPersonas();
        }
    }

    private void crearPaises() {
        paisService.c("España");
        paisService.c("Alemania");
        paisService.c("Francia");
    }

    private void crearRoles() {
        rolService.c("admin");
        rolService.c("user");
    }

    private void crearPersonas() {
       personaService.c("Admin", "Ape-admin", "admin", "admin", "admin", null, null, new ArrayList<Long>(), new ArrayList<Long>());
    }

    private void crearAficiones() {
        aficionService.c("Deportes");
        aficionService.c("Cine");
        aficionService.c("TV");
    }
}
