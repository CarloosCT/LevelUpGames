package org.agaray.pruebas.pap.controller.rest;

import java.util.List;

import org.agaray.pruebas.pap.entities.Persona;
import org.agaray.pruebas.pap.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persona")
public class PersonaRestController {
    
    @Autowired
    private PersonaService personaService;

    @RequestMapping("r")
    public List<Persona> r() {
        return personaService.r();
    }
}
