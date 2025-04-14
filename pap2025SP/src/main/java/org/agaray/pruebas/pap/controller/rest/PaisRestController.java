package org.agaray.pruebas.pap.controller.rest;

import java.util.List;

import org.agaray.pruebas.pap.entities.Pais;
import org.agaray.pruebas.pap.services.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pais")
public class PaisRestController {
    

    @Autowired
    private PaisService paisService;

    @RequestMapping("r")
    public List<Pais> r() {
        return paisService.r();
    }
}
